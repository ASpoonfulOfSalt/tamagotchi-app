package com.cse.tamagotchi.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Tamagotchi

@Composable
fun PetStatsSection(t: Tamagotchi) {
    Box(
        modifier = Modifier.fillMaxWidth(0.75f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            StatBar(
                label = "Hunger",
                emoji = "🍎",
                value = t.hunger,
                fillColor = Color(0xFF00FF00) // warm orange
            )
            StatBar(
                label = "Water",
                emoji = "💧",
                value = t.water,
                fillColor = Color(0xFF4DB6FF) // soft blue
            )
            StatBar(
                label = "Happiness",
                emoji = "😄",
                value = t.happiness,
                fillColor = Color(0xFFFFD54F) // gold/yellow
            )
        }
    }
}
