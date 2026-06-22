package com.mariolkotsiai.seismowatch.data.remote.model

import com.google.gson.annotations.SerializedName

data class NasaEonetResponse(
    @SerializedName("events") val events: List<EonetEvent>
)

data class EonetEvent(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("categories") val categories: List<EonetCategory>,
    @SerializedName("geometries") val geometries: List<EonetGeometry>
)

data class EonetCategory(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String
)

data class EonetGeometry(
    @SerializedName("coordinates") val coordinates: List<Double>, // [longitude, latitude]
    @SerializedName("date") val date: String
)