package com.cse.tamagotchi.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun RenamePetDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Pet") },
        text = {
            OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("New Name") })
        },
        confirmButton = {
            Button(onClick = { if (text.isNotBlank()) onConfirm(text) }) {
                Text("Save")
            }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}
