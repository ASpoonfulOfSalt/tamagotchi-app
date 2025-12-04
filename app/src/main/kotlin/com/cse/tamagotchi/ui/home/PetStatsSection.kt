package com.cse.tamagotchi.ui.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cse.tamagotchi.model.Tamagotchi

@Composable
fun PetStatsSection(t: Tamagotchi) {
    Text(
        text = "Hunger: ${t.hunger}",
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = "Water: ${t.water}",
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = "Happiness: ${t.happiness}",
        color = MaterialTheme.colorScheme.onBackground
    )
}
