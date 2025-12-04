package com.cse.tamagotchi.ui.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.cse.tamagotchi.model.Tamagotchi

@Composable
fun StreakDisplay(t: Tamagotchi) {
    Text(
        text = "🔥 Daily Streak: ${t.streakCount}",
        color = MaterialTheme.colorScheme.onBackground)
}
