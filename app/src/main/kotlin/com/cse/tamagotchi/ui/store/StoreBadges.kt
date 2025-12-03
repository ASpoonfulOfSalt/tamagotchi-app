package com.cse.tamagotchi.ui.store

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.LightModeGreen

@Composable
fun PriceBadge(price: Int, isDarkMode: Boolean) {
    Badge(
        containerColor = if (isDarkMode) DarkModeGreen.copy(alpha = 0.8f)
        else LightModeGreen.copy(alpha = 0.8f),
        contentColor = Color.Black
    ) {
        Text("$price coins")
    }
}

@Composable
fun OwnedBadge(quantity: Int) {
    Badge(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text("Owned: $quantity")
    }
}
