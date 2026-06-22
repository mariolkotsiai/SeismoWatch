package com.mariolkotsiai.seismowatch.ui.screens.eventlist

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.mariolkotsiai.seismowatch.data.repository.DisasterRepository
import com.mariolkotsiai.seismowatch.domain.model.DisasterEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    object Loading : UiState
    data class Success(val events: List<DisasterEvent>) : UiState
    data class Error(val message: String) : UiState
}

class EventListViewModel(
    private val repository: DisasterRepository = DisasterRepository(),
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchDisastersForUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchDisastersForUserLocation() {
        _uiState.value = UiState.Loading
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        loadDisasters(location.latitude, location.longitude)
                    } else {
                        loadDisasters(37.9838, 23.7275) // Default: Αθήνα
                    }
                }
                .addOnFailureListener { exception ->
                    _uiState.value = UiState.Error(exception.localizedMessage ?: "Σφάλμα τοποθεσίας.")
                }
        } catch (e: SecurityException) {
            loadDisasters(37.9838, 23.7275)
        }
    }

    private fun loadDisasters(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val disasters = repository.fetchAllDisasters(lat, lon)
                _uiState.value = UiState.Success(disasters)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Σφάλμα δικτύου.")
            }
        }
    }
}