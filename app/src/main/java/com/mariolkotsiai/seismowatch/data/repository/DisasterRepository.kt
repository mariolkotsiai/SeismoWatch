package com.mariolkotsiai.seismowatch.data.repository

import com.mariolkotsiai.seismowatch.data.remote.RetrofitInstance
import com.mariolkotsiai.seismowatch.data.remote.api.EarthquakeApiService
import com.mariolkotsiai.seismowatch.data.remote.api.NasaEonetApiService
import com.mariolkotsiai.seismowatch.domain.model.DisasterEvent
import com.mariolkotsiai.seismowatch.domain.model.DisasterType
import com.mariolkotsiai.seismowatch.domain.usecase.ImpactCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DisasterRepository(
    private val earthquakeApi: EarthquakeApiService = RetrofitInstance.earthquakeApi,
    private val nasaEonetApi: NasaEonetApiService = RetrofitInstance.nasaEonetApi,
    private val impactCalculator: ImpactCalculator = ImpactCalculator()
) {

    suspend fun fetchAllDisasters(userLat: Double, userLon: Double): List<DisasterEvent> = withContext(Dispatchers.IO) {
        val earthquakesDeferred = async { runCatching { earthquakeApi.getEarthquakes() }.getOrNull() }
        val eonetDeferred = async { runCatching { nasaEonetApi.getEvents() }.getOrNull() }

        val usgsResponse = earthquakesDeferred.await()
        val eonetResponse = eonetDeferred.await()

        val combinedEvents = mutableListOf<DisasterEvent>()

        // USGS Parsing
        usgsResponse?.features?.forEach { feature ->
            val coords = feature.geometry.coordinates
            if (coords.size >= 2) {
                val lon = coords[0]
                val lat = coords[1]
                val mag = feature.properties.mag ?: 1.0
                val distance = impactCalculator.calculateDistance(userLat, userLon, lat, lon)
                val impact = impactCalculator.calculateImpactPercentage(mag, distance, DisasterType.EARTHQUAKE)

                combinedEvents.add(
                    DisasterEvent(
                        id = "usgs_${feature.properties.time ?: System.currentTimeMillis()}",
                        title = feature.properties.place ?: "Σεισμική Δόνηση",
                        type = DisasterType.EARTHQUAKE,
                        latitude = lat,
                        longitude = lon,
                        severity = mag,
                        timestamp = feature.properties.time ?: 0L,
                        distanceKm = distance,
                        impactPercentage = impact
                    )
                )
            }
        }

        // NASA EONET Parsing
        eonetResponse?.events?.forEach { event ->
            val geometry = event.geometries.firstOrNull()
            val coords = geometry?.coordinates
            if (coords != null && coords.size >= 2) {
                val lon = coords[0]
                val lat = coords[1]
                val type = if (event.categories.any { it.id == "wildfires" }) DisasterType.FIRE else DisasterType.STORM

                val severity = if (type == DisasterType.FIRE) 7.0 else 6.0
                val distance = impactCalculator.calculateDistance(userLat, userLon, lat, lon)
                val impact = impactCalculator.calculateImpactPercentage(severity, distance, type)

                combinedEvents.add(
                    DisasterEvent(
                        id = event.id,
                        title = event.title,
                        type = type,
                        latitude = lat,
                        longitude = lon,
                        severity = severity,
                        timestamp = System.currentTimeMillis(),
                        distanceKm = distance,
                        impactPercentage = impact
                    )
                )
            }
        }

        combinedEvents.sortByDescending { it.impactPercentage }
        combinedEvents
    }
}