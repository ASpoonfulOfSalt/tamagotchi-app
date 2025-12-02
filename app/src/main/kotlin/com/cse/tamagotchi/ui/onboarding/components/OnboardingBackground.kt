package com.cse.tamagotchi.ui.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun OnboardingBackground(
    prefersDark: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (prefersDark) {
        listOf(
            Color(0xFF0D0D0D),
            Color(0xFF1A1A1A),
            Color(0xFF262626)
        )
    } else {
        listOf(
            Color(0xFFFFF3E0),
            Color(0xFFFFE0F0),
            Color(0xFFE0F7FF)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors))
    ) {
        content()
    }
}

