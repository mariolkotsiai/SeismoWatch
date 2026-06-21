package com.mariolkotsiai.seismowatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mariolkotsiai.seismowatch.ui.screens.eventlist.EventListScreen
import com.mariolkotsiai.seismowatch.ui.theme.SeismoWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeismoWatchTheme {
                // Surface: Το βασικό φόντο της εφαρμογής που παίρνει το χρώμα του θέματος
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Καλούμε την κύρια οθόνη που δείχνει τη λίστα των σεισμών
                    EventListScreen()
                }
            }
        }
    }
}