package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.theme.StatsChartBaseDark
import com.cse.tamagotchi.ui.theme.StatsChartBaseLight
import com.cse.tamagotchi.ui.theme.StatsChartFillDark
import com.cse.tamagotchi.ui.theme.StatsChartFillLight

@Composable
fun StatsProgressRing(
    streak: Int,
    bestStreak: Int
) {
    // Visual target: 30-day legendary streak
    val legendaryTarget = 30f
    val percentage = (streak.toFloat() / legendaryTarget).coerceIn(0f, 1f)

    val dark = isSystemInDarkTheme()

    val baseColor =
        if (dark) StatsChartBaseDark else StatsChartBaseLight

    val fillColor =
        if (dark) StatsChartFillDark else StatsChartFillLight

    val textColor = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier.size(170.dp)
        ) {
            val stroke = 22f

            // Background ring
            drawArc(
                color = baseColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
            )

            // Progress ring
            drawArc(
                color = fillColor,
                startAngle = -90f,
                sweepAngle = 360 * percentage,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "🔥 Current Streak: $streak",
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        Text(
            text = "🏆 Best streak: $bestStreak",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "✨ Legendary at 30 days",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
