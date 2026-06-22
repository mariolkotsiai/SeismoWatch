package com.mariolkotsiai.seismowatch.domain.usecase

import com.mariolkotsiai.seismowatch.domain.model.DisasterType
import kotlin.math.*

class ImpactCalculator {

    // Φόρμουλα Haversine για απόσταση μεταξύ δύο συντεταγμένων
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Ακτίνα της Γης σε χιλιόμετρα
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    // Υπολογισμός Ποσοστού Κινδύνου / Επηρεασμού
    fun calculateImpactPercentage(severity: Double, distanceKm: Double, type: DisasterType): Double {
        val maxRadius = when (type) {
            DisasterType.EARTHQUAKE -> 400.0
            DisasterType.FIRE -> 250.0
            DisasterType.STORM -> 500.0
        }

        if (distanceKm > maxRadius) return 0.0

        // 40% βαρύτητα στο μέγεθος του συμβάντος, 60% στην απόσταση
        val severityWeight = (severity.coerceIn(1.0, 10.0) / 10.0)
        val distanceWeight = (1.0 - (distanceKm / maxRadius)).coerceIn(0.0, 1.0)

        val finalImpact = (severityWeight * 0.4 + distanceWeight * 0.6) * 100
        return round(finalImpact * 10) / 10.0
    }
}