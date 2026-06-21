package com.mariol.seismowatch.data.remote.api

import com.mariol.seismowatch.data.remote.model.EarthquakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface για το USGS Earthquake API.
 * Base URL: https://earthquake.usgs.gov/
 */
interface EarthquakeApiService {

    @GET("fdsnws/event/1/query")
    suspend fun getRecentEarthquakes(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,      // π.χ. "2026-06-14"
        @Query("minmagnitude") minMagnitude: Double = 2.5,
        @Query("orderby") orderBy: String = "time"
    ): EarthquakeResponse
}