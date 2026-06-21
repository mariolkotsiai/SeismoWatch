package com.mariol.seismowatch.data.remote

import com.mariol.seismowatch.data.remote.api.EarthquakeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // βλέπεις το raw JSON στο Logcat
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val earthquakeApi: EarthquakeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EarthquakeApiService::class.java)
    }
}