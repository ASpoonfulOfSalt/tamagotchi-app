package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsMiniChart(
    title: String,
    current: Int,
    max: Int
) {
    val bgColor = MaterialTheme.colorScheme.surfaceVariant
    val fgColor = MaterialTheme.colorScheme.primary
    val titleColor = MaterialTheme.colorScheme.onBackground
    val valueColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = titleColor
        )
        Spacer(Modifier.height(6.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
        ) {
            val progress = (current.toFloat() / max).coerceIn(0f, 1f)
            val barWidth = size.width * progress

            drawRoundRect(
                color = bgColor,
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
            )

            drawRoundRect(
                color = fgColor,
                size = androidx.compose.ui.geometry.Size(barWidth, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "$current / $max min",
            style = MaterialTheme.typography.bodySmall,
            color = valueColor
        )
    }
}
