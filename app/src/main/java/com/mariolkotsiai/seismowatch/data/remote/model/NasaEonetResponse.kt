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
    @SerializedName("type") val type: String?,
    @SerializedName("coordinates") val coordinates: Any?, // Gson θα φέρει ό,τι υπάρχει
    @SerializedName("date") val date: String
) {
    fun getLonLat(): Pair<Double, Double>? {
        return when (val c = coordinates) {
            is List<*> -> {
                val first = c.firstOrNull()
                if (first is Double) {
                    // Format: [lon, lat]
                    val lon = c.getOrNull(0) as? Double ?: return null
                    val lat = c.getOrNull(1) as? Double ?: return null
                    Pair(lon, lat)
                } else if (first is List<*>) {
                    // Format: [[lon, lat], ...]
                    val inner = first
                    val lon = inner.getOrNull(0) as? Double ?: return null
                    val lat = inner.getOrNull(1) as? Double ?: return null
                    Pair(lon, lat)
                } else null
            }
            else -> null
        }
    }
}