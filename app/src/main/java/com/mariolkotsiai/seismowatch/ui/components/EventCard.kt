package com.mariolkotsiai.seismowatch.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mariolkotsiai.seismowatch.domain.model.DisasterEvent
import com.mariolkotsiai.seismowatch.domain.model.DisasterType

@Composable
fun EventCard(event: DisasterEvent) {
    val isHighRisk = event.impactPercentage >= 65.0

    val cardColors = if (isHighRisk) {
        CardDefaults.cardColors(containerColor = Color(0xFFFEEBEE))
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    }

    val cardBorder = if (isHighRisk) BorderStroke(2.dp, Color.Red) else null

    val typeColor = when (event.type) {
        DisasterType.EARTHQUAKE -> Color(0xFF8B0000)
        DisasterType.FIRE -> Color(0xFFE65100)
        DisasterType.STORM -> Color(0xFF0D47A1)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = cardColors,
        border = cardBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.type.name,
                    fontWeight = FontWeight.Black,
                    color = typeColor,
                    fontSize = 14.sp
                )

                if (isHighRisk) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ΚΙΝΔΥΝΟΣ",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Απόσταση: ${String.format("%.1f", event.distanceKm)} km",
                    fontSize = 14.sp
                )
                Text(
                    text = "Επηρεασμός: ${event.impactPercentage}%",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isHighRisk) Color.Red else Color.Unspecified
                )
            }
        }
    }
}