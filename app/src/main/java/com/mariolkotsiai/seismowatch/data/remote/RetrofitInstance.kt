package com.mariolkotsiai.seismowatch.data.remote

import com.mariolkotsiai.seismowatch.data.remote.api.EarthquakeApiService
import com.mariolkotsiai.seismowatch.data.remote.api.NasaEonetApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val USGS_BASE_URL = "https://earthquake.usgs.gov/"
    private const val NASA_BASE_URL = "https://eonet.sci.gsfc.nasa.gov/"

    val earthquakeApi: EarthquakeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(USGS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EarthquakeApiService::class.java)
    }

    val nasaEonetApi: NasaEonetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NASA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaEonetApiService::class.java)
    }
}