package com.cse.tamagotchi.ui.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PetBounce(
    emoji: String = "🐣" // replace with sprite later if needed
) {
    val anim = rememberInfiniteTransition()
    val offset by anim.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Text(
        text = emoji,
        fontSize = 80.sp,
        modifier = Modifier.offset(y = offset.dp)
    )
}
