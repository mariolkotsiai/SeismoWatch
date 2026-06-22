package com.mariolkotsiai.seismowatch.data.remote.model

import com.google.gson.annotations.SerializedName

data class EarthquakeResponse(
    @SerializedName("features") val features: List<UsgsFeature>
)

data class UsgsFeature(
    @SerializedName("properties") val properties: UsgsProperties,
    @SerializedName("geometry") val geometry: UsgsGeometry
)

data class UsgsProperties(
    @SerializedName("mag") val mag: Double?,
    @SerializedName("place") val place: String?,
    @SerializedName("time") val time: Long?
)

data class UsgsGeometry(
    @SerializedName("coordinates") val coordinates: List<Double> // [longitude, latitude, depth]
)