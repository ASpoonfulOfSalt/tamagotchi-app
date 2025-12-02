package com.cse.tamagotchi.ui.onboarding.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemePage(
    prefersDark: Boolean,
    onSelect: (Boolean) -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choose your theme", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(32.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Light option
            ThemeOption(
                selected = !prefersDark,
                emoji = "☀️",
                label = "Light",
                onClick = { onSelect(false) }
            )

            // Dark option
            ThemeOption(
                selected = prefersDark,
                emoji = "🌙",
                label = "Dark",
                onClick = { onSelect(true) }
            )
        }
    }
}

@Composable
fun ThemeOption(
    selected: Boolean,
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(if (selected) 1.3f else 1f)
    val bg = if (selected) Color(0x33000000) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(12.dp)
            .defaultMinSize(minWidth = 110.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .background(bg)
            .padding(12.dp)
    ) {
        Text(emoji, fontSize = 48.sp)
        Spacer(Modifier.height(8.dp))
        Text(label)
    }
}
