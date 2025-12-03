package com.cse.tamagotchi.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StatBubbleData(
    val label: String,
    val emoji: String,
    val value: String,
    val subtitle: String
)

@Composable
fun StatsBubble(
    data: StatBubbleData,
    modifier: Modifier = Modifier
) {
    val bubbleColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
    val labelColor = MaterialTheme.colorScheme.onSurface
    val valueColor = MaterialTheme.colorScheme.onSurface // HIGH contrast for numbers
    val subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = bubbleColor
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Label (with emoji)
            Text(
                text = "${data.emoji} ${data.label}",
                style = MaterialTheme.typography.titleMedium,
                color = labelColor
            )

            Spacer(Modifier.height(6.dp))

            // Main stat number
            Text(
                text = data.value,
                style = MaterialTheme.typography.headlineSmall,
                color = valueColor
            )

            Spacer(Modifier.height(4.dp))

            // Subtitle description
            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
    }
}

