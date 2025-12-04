package com.cse.tamagotchi.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Task
import com.cse.tamagotchi.ui.theme.*

@Composable
fun TaskItem(
    task: Task,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val baseColor = if (isDarkMode) Color(0xAA1F1F1F) else Color(0xAAFFFFFF)
    val dailyColor = if (isDarkMode) Color(0xAA1F2E4F) else Color(0xAAE0F0FF)
    val weeklyColor = if (isDarkMode) Color(0x552E4F1F) else Color(0x55CCFF80)

    val bubbleColor = if (task.isDaily) dailyColor else weeklyColor

    val textColor = if (task.isCompleted)
        (if (isDarkMode) PureWhite.copy(alpha = 0.4f) else DarkGrey.copy(alpha = 0.4f))
    else
        (if (isDarkMode) PureWhite else DarkGrey)

    val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
            .background(color = bubbleColor, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium.copy(textDecoration = textDecoration),
                color = textColor
            )

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isDarkMode) DarkModeGreen else LightModeGreen
                )
            )
        }

        Spacer(Modifier.height(4.dp))

        if (task.description.isNotBlank()) {
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall.copy(textDecoration = textDecoration),
                color = textColor.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(6.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("💰 ${task.currencyReward}", color = textColor)
            Spacer(Modifier.width(12.dp))

            val difficulty = (task.currencyReward / 10).coerceIn(1, 5)
            val stars = "⭐".repeat(difficulty) + "☆".repeat(5 - difficulty)

            Text(stars, color = textColor)

            Spacer(Modifier.width(8.dp))
            Text(
                if (task.isDaily) "Daily" else "Weekly",
                color = if (task.isDaily)
                    (if (isDarkMode) Color(0xFF80D0FF) else Color(0xFF0077CC))
                else
                    (if (isDarkMode) Color(0xFFFFB066) else Color(0xFFFF7700))
            )
        }
    }
}
