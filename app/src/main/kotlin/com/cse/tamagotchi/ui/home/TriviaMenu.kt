package com.cse.tamagotchi.ui.home

import androidx.compose.runtime.*
import com.cse.tamagotchi.ui.TriviaMinigameDialog

@Composable
fun TriviaMenuDialog(
    onDismiss: () -> Unit,
    onFinish: (correctAnswers: Int) -> Unit
) {
    // You likely already have TriviaMinigameDialog — call that here
    TriviaMinigameDialog(
        onDismiss = onDismiss,
        onGameFinished = { onFinish(it) }
    )
}
