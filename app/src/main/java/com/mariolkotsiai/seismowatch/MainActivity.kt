package com.mariolkotsiai.seismowatch

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.LocationServices
import com.mariolkotsiai.seismowatch.ui.screens.eventlist.EventListScreen
import com.mariolkotsiai.seismowatch.ui.screens.eventlist.EventListViewModel
import com.mariolkotsiai.seismowatch.ui.theme.SeismoWatchTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Απλή αρχικοποίηση ViewModel (χωρίς DI/Hilt για ευκολία)
        val viewModel = EventListViewModel(fusedLocationClient = fusedLocationClient)

        // Ζητάμε άδεια τοποθεσίας με το που ανοίγει η εφαρμογή
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                viewModel.fetchDisastersForUserLocation()
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            SeismoWatchTheme {
                EventListScreen(viewModel = viewModel)
            }
        }
    }
}