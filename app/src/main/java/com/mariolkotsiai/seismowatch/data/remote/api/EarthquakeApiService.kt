package com.mariolkotsiai.seismowatch.data.remote.api

import com.mariolkotsiai.seismowatch.data.remote.model.EarthquakeResponse
import retrofit2.http.GET

interface EarthquakeApiService {
    @GET("fdsnws/event/1/query?format=geojson&limit=50")
    suspend fun getEarthquakes(): EarthquakeResponse
}