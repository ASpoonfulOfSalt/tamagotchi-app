package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.StatsUiState
import java.util.Locale

@Composable
fun StatsBubbleGrid(
    stats: StatsUiState,
    installDateText: String,
    bestStreak: Int
) {
    val tasksPerDay = if (stats.daysUsed > 0) {
        stats.totalTasksCompleted.toFloat() / stats.daysUsed
    } else 0f

    val tasksPerDayText = String.format(Locale.US, "%.1f", tasksPerDay)

    val bubbles = listOf(
        StatBubbleData(
            label = "Level",
            emoji = "⭐",
            value = stats.level.toString(),
            subtitle = "Your current HabitGotchi level"
        ),
        StatBubbleData(
            label = "XP",
            emoji = "⚡",
            value = stats.xp.toString(),
            subtitle = "Total experience gained"
        ),
        StatBubbleData(
            label = "Hats Owned",
            emoji = "🎩",
            value = stats.hatCount.toString(),
            subtitle = "Because *someone's* gotta keep count"
        ),
        StatBubbleData(
            label = "Current Streak",
            emoji = "🔁",
            value = stats.currentStreak.toString(),
            subtitle = "How many days in a row!"
        ),
        StatBubbleData(
            label = "Best Streak",
            emoji = "🔥",
            value = bestStreak.toString(),
            subtitle = "Your all-time record"
        ),
        StatBubbleData(
            label = "Tasks Completed",
            emoji = "✅",
            value = stats.totalTasksCompleted.toString(),
            subtitle = "Total tasks finished"
        ),
        StatBubbleData(
            label = "Coins Earned",
            emoji = "💰",
            value = stats.totalCoinsEarned.toString(),
            subtitle = "All currency collected"
        ),
        StatBubbleData(
            label = "Tasks / Day",
            emoji = "📈",
            value = tasksPerDayText,
            subtitle = "Average productivity"
        ),
        StatBubbleData(
            label = "Days Used",
            emoji = "📅",
            value = stats.daysUsed.toString(),
            subtitle = "Active HabitGotchi days"
        ),
        StatBubbleData(
            label = "Started On",
            emoji = "🌱",
            value = installDateText,
            subtitle = "Your HabitGotchi’s birthday"
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        bubbles.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatsBubble(
                    data = rowItems[0],
                    modifier = Modifier.weight(1f)
                )

                if (rowItems.size > 1) {
                    StatsBubble(
                        data = rowItems[1],
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
