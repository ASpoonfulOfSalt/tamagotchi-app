package com.cse.tamagotchi.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun XpBarSection(
    level: Int,
    xp: Int,
    showXpMenu: Boolean,
    onXpMenuClick: () -> Unit,
    onDismissXpMenu: () -> Unit,
    onPlayTrivia: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onXpMenuClick() },
        contentAlignment = Alignment.Center
    ) {
        XpBar(
            xp = xp,
            level = level,
            modifier = Modifier.fillMaxWidth(0.85f)
        )

        DropdownMenu(
            expanded = showXpMenu,
            onDismissRequest = onDismissXpMenu
        ) {
            DropdownMenuItem(
                text = { Text("Use Book for Minigame") },
                onClick = {
                    onDismissXpMenu()
                    onPlayTrivia()
                }
            )
        }
    }
}

@Composable
fun XpBar(xp: Int, level: Int, modifier: Modifier = Modifier) {
    val xpNeeded = level * 100
    val progress = xp.toFloat() / xpNeeded.toFloat()

    Column(modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Level: $level",
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$xp / $xpNeeded XP",
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = ProgressIndicatorDefaults.linearTrackColor,
            trackColor = ProgressIndicatorDefaults.linearColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}
