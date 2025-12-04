package com.cse.tamagotchi.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun LevelUpDialog(reward: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Congrats!") },
        text = { Text("You leveled up! You received $reward coins!") },
        confirmButton = { Button(onClick = onDismiss) { Text("Awesome!") } }
    )
}
