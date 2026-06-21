package com.mariol.seismowatch.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Το top-level αντικείμενο που επιστρέφει το USGS API.
 * Περιέχει μια λίστα από σεισμικά γεγονότα (features).
 */
data class EarthquakeResponse(
    val type: String,
    val features: List<EarthquakeFeature>
)

/**
 * Ένα μεμονωμένο σεισμικό γεγονός.
 */
data class EarthquakeFeature(
    val id: String,
    val properties: EarthquakeProperties,
    val geometry: EarthquakeGeometry
)

/**
 * Οι "ιδιότητες" του σεισμού: μέγεθος, τοποθεσία (περιγραφή), ώρα κ.λπ.
 */
data class EarthquakeProperties(
    @SerializedName("mag") val magnitude: Double?,   // π.χ. 4.5 (Ρίχτερ)
    @SerializedName("place") val place: String?,     // π.χ. "10km N of Athens, Greece"
    @SerializedName("time") val timeMillis: Long,     // Unix timestamp σε ms
    @SerializedName("url") val detailUrl: String?,
    @SerializedName("tsunami") val tsunami: Int,      // 1 = υπάρχει προειδοποίηση τσουνάμι
    @SerializedName("title") val title: String?
)

/**
 * Οι γεωγραφικές συντεταγμένες του επικέντρου.
 * ΠΡΟΣΟΧΗ: το USGS δίνει [longitude, latitude, depth] — ΟΧΙ [lat, lon]!
 */
data class EarthquakeGeometry(
    val type: String,
    val coordinates: List<Double>
) {
    val longitude: Double get() = coordinates[0]
    val latitude: Double get() = coordinates[1]
    val depthKm: Double get() = coordinates.getOrElse(2) { 0.0 }
}
