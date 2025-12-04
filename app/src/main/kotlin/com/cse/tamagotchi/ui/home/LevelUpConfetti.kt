package com.cse.tamagotchi.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

@Composable
fun LevelUpConfetti(modifier: Modifier = Modifier, onFinished: () -> Unit) {
    var started by remember { mutableStateOf(false) }
    val particles = remember { List(150) { ConfettiParticle() } }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(3000),
        finishedListener = { onFinished() },
        label = "confetti"
    )

    LaunchedEffect(Unit) { started = true }

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        for (p in particles) {
            val (pos, alpha, rot) = p.update(progress, w, h)
            if (alpha > 0) rotate(rot, pos) {
                drawRect(p.color, pos, p.size, alpha)
            }
        }
    }
}

data class ConfettiParticle(
    val color: Color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f),
    val startX: Float = Random.nextFloat(),
    val startY: Float = Random.nextFloat() * -0.2f,
    val vx: Float = Random.nextFloat() * 200 - 100,
    val vy: Float = Random.nextFloat() * 300 + 400,
    val rotSpeed: Float = Random.nextFloat() * 720 - 360,
    val size: androidx.compose.ui.geometry.Size =
        androidx.compose.ui.geometry.Size(Random.nextFloat() * 20 + 15, Random.nextFloat() * 10 + 10)
) {
    fun update(progress: Float, w: Float, h: Float): Triple<Offset, Float, Float> {
        val t = progress * 2.5f
        val x = startX * w + vx * t
        val y = startY * h + vy * t
        val alpha = (1f - progress).coerceIn(0f, 1f)
        val rot = rotSpeed * t
        return Triple(Offset(x, y), alpha, rot)
    }
}
