package com.mariolkotsiai.seismowatch.data.remote.api

import com.mariolkotsiai.seismowatch.data.remote.model.NasaEonetResponse
import retrofit2.http.GET

interface NasaEonetApiService {
    @GET("api/v3/events?status=open&limit=50")
    suspend fun getEvents(): NasaEonetResponse
}