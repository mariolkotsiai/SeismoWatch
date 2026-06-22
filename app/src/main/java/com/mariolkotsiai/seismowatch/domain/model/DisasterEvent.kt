package com.mariolkotsiai.seismowatch.domain.model

enum class DisasterType {
    EARTHQUAKE, FIRE, STORM
}

data class DisasterEvent(
    val id: String,
    val title: String,
    val type: DisasterType,
    val latitude: Double,
    val longitude: Double,
    val severity: Double,
    val timestamp: Long,
    val distanceKm: Double = 0.0,
    val impactPercentage: Double = 0.0
)