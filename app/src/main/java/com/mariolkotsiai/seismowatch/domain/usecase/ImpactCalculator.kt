package com.mariol.seismowatch.domain.usecase

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Τα επίπεδα επίδρασης που θα δούμε στο UI.
 */
enum class ImpactLevel(val label: String) {
    DANGER("Κίνδυνος / Έντονα Αισθητός"),
    FELT("Αισθητός"),
    MINOR("Μικρή Επίδραση"),
    NONE("Καμία Επίδραση")
}

object ImpactCalculator {

    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Υπολογίζει την απόσταση (σε km) ανάμεσα σε δύο σημεία πάνω στη Γη,
     * χρησιμοποιώντας τον τύπο Haversine. Παίρνει υπόψη ότι η Γη είναι σφαίρα,
     * όχι επίπεδη — γι' αυτό δεν αρκεί ένα απλό Πυθαγόρειο θεώρημα.
     */
    fun calculateDistanceKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }

    /**
     * Συνδυάζει απόσταση + μέγεθος σεισμού για να αποφασίσει πόσο "σοβαρό" είναι
     * το γεγονός ΓΙΑ ΤΟΝ ΣΥΓΚΕΚΡΙΜΕΝΟ ΧΡΗΣΤΗ.
     *
     * Η λογική εδώ είναι μια απλοποιημένη ευρετική (heuristic) -- ΔΕΝ είναι
     * επιστημονικά ακριβής σεισμολογική πρόβλεψη, απλώς ένα χρήσιμο σημείο
     * εκκίνησης για το UX της εφαρμογής.
     */
    fun getEarthquakeImpact(
        userLat: Double, userLon: Double,
        quakeLat: Double, quakeLon: Double,
        magnitude: Double
    ): ImpactLevel {
        val distanceKm = calculateDistanceKm(userLat, userLon, quakeLat, quakeLon)

        // Η "ακτίνα επιρροής" μεγαλώνει εκθετικά με το μέγεθος.
        // Αυτό προσομοιώνει ότι ένας σεισμός 6.0 γίνεται αισθητός πολύ
        // μακρύτερα από έναν 3.0.
        return when {
            magnitude >= 6.0 && distanceKm <= 300 -> ImpactLevel.DANGER
            magnitude >= 5.0 && distanceKm <= 150 -> ImpactLevel.DANGER
            magnitude >= 4.0 && distanceKm <= 100 -> ImpactLevel.FELT
            magnitude >= 3.0 && distanceKm <= 50  -> ImpactLevel.FELT
            magnitude >= 2.5 && distanceKm <= 20   -> ImpactLevel.MINOR
            distanceKm <= 500                       -> ImpactLevel.MINOR
            else -> ImpactLevel.NONE
        }
    }
}