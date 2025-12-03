package com.cse.tamagotchi.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.StatsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatsScreen(
    tamagotchiViewModel: com.cse.tamagotchi.viewmodel.TamagotchiViewModel,
    taskViewModel: com.cse.tamagotchi.viewmodel.TaskViewModel,
    userPrefs: com.cse.tamagotchi.repository.UserPreferencesRepository,
    viewModel: StatsViewModel
) {
    val stats by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Your Progress",
            style = MaterialTheme.typography.headlineMedium
        )

        StatsCard(title = "Days Used", value = stats.daysUsed.toString())
        StatsCard(title = "Best Streak", value = stats.bestStreak.toString())
        StatsCard(title = "Tasks Completed", value = stats.totalTasksCompleted.toString())
        StatsCard(title = "Coins Earned", value = stats.totalCoinsEarned.toString())
        StatsCard(
            title = "Install Date",
            value = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(stats.installDate))
        )
        StatsCard(
            title = "Time Played",
            value = "${stats.totalMinutesUsed} min"
        )
    }
}

@Composable
fun StatsCard(title: String, value: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
