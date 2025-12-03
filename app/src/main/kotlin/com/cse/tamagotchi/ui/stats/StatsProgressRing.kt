package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsProgressRing(
    streak: Int,
    bestStreak: Int
) {
    val percentage = if (bestStreak == 0) 0f else (streak.toFloat() / bestStreak).coerceIn(0f, 1f)

    // Get colors BEFORE Canvas
    val baseColor = MaterialTheme.colorScheme.statsRingBackground
    val progressColor = MaterialTheme.colorScheme.statsRingProgress
    val textColor = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val strokeWidth = 20f

            drawArc(
                color = baseColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "🔥 Current Streak: $streak",
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        Text(
            text = "🏆 Best: $bestStreak",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
