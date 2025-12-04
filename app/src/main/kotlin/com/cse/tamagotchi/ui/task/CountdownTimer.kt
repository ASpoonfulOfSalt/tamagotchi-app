package com.cse.tamagotchi.ui.task

import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(nextResetTime: Long) {
    var timeRemaining by remember { mutableStateOf("") }

    LaunchedEffect(nextResetTime) {
        while (true) {
            val remaining = nextResetTime - System.currentTimeMillis()
            if (remaining > 0) {
                val hours = remaining / (1000 * 60 * 60)
                val minutes = (remaining % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (remaining % (1000 * 60)) / 1000
                timeRemaining = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                timeRemaining = "00:00:00"
            }
            delay(1000)
        }
    }

    Text(
        text = "Tasks reset in: $timeRemaining",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
    )
}
