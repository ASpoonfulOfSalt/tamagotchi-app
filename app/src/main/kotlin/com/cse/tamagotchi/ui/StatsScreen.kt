package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.stats.StatsBubbleGrid
import com.cse.tamagotchi.ui.stats.StatsHeader
import com.cse.tamagotchi.ui.stats.StatsMiniChart
import com.cse.tamagotchi.ui.stats.StatsProgressRing
import com.cse.tamagotchi.viewmodel.StatsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

@Composable
fun StatsScreen(
    viewModel: StatsViewModel
) {
    val stats by viewModel.uiState.collectAsState()

    // Keep best streak in sync with current streak
    LaunchedEffect(stats.currentStreak, stats.bestStreak) {
        if (stats.currentStreak > stats.bestStreak) {
            viewModel.updateBestStreak(stats.currentStreak)
        }
    }

    val installDateText = remember(stats.installDate) {
        if (stats.installDate == 0L) {
            "Just started!"
        } else {
            SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(stats.installDate))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StatsHeader()

        Text(
            text = "Trends",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        StatsProgressRing(
            streak = stats.currentStreak,
            bestStreak = stats.bestStreak
        )

        // XP bar: progress toward next level
        val xpForNextLevel = 100 * stats.level

        StatsMiniChart(
            title = "XP to Next Level",
            current = stats.xp,
            max = xpForNextLevel
        )


        StatsBubbleGrid(
            stats = stats,
            installDateText = installDateText,
            bestStreak = stats.bestStreak
        )
    }
}
