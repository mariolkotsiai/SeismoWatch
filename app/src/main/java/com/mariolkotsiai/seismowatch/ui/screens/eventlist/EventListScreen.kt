package com.mariolkotsiai.seismowatch.ui.screens.eventlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mariolkotsiai.seismowatch.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(viewModel: EventListViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SeismoWatch") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Σφάλμα: ${state.message}", modifier = Modifier.padding(16.dp))
                    Button(onClick = { viewModel.fetchDisastersForUserLocation() }) {
                        Text("Επαναδοκιμή")
                    }
                }
                is UiState.Success -> {
                    if (state.events.isEmpty()) {
                        Text("Δεν υπάρχουν τρέχουσες φυσικές καταστροφές κοντά σας.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.events, key = { it.id }) { event ->
                                EventCard(event = event)
                            }
                        }
                    }
                }
            }
        }
    }
}