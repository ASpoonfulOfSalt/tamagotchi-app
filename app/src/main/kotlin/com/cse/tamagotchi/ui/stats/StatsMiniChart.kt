package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.theme.StatsChartBaseDark
import com.cse.tamagotchi.ui.theme.StatsChartBaseLight
import com.cse.tamagotchi.ui.theme.StatsChartFillDark
import com.cse.tamagotchi.ui.theme.StatsChartFillLight

@Composable
fun StatsMiniChart(
    title: String,
    current: Int,
    max: Int
) {
    val dark = isSystemInDarkTheme()

    val baseColor =
        if (dark) StatsChartBaseDark else StatsChartBaseLight

    val fillColor =
        if (dark) StatsChartFillDark else StatsChartFillLight

    val textColor = MaterialTheme.colorScheme.onBackground

    val fraction = if (max <= 0) 0f else (current.toFloat() / max).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
        ) {
            val cornerRadius = 20f
            val barWidth = size.width * fraction

            // Background bar
            drawRoundRect(
                color = baseColor,
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    cornerRadius,
                    cornerRadius
                )
            )

            // Filled portion
            drawRoundRect(
                color = fillColor,
                size = androidx.compose.ui.geometry.Size(barWidth, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    cornerRadius,
                    cornerRadius
                )
            )
        }

        Text(
            text = "$current / $max XP to next level",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
