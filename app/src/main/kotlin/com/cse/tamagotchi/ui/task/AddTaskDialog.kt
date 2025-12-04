package com.cse.tamagotchi.ui.task

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cse.tamagotchi.ui.theme.*

@Composable
fun AddTaskDialog(
    isVisible: Boolean,
    taskName: String,
    isDarkMode: Boolean,
    onNameChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!isVisible) return

    // Allow letters, digits, and spaces only; cap to 30 chars
    fun sanitizeInput(input: String): String {
        val alphanumericOnly = input.filter { it.isLetterOrDigit() || it.isWhitespace() }
        return alphanumericOnly.take(30)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a New Task") },
        text = {
            OutlinedTextField(
                value = taskName,
                onValueChange = { newValue ->
                    onNameChanged(sanitizeInput(newValue))
                },
                label = { Text("Task Name (Max 30 chars)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("${taskName.length}/30")
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                    contentColor = if (isDarkMode) PureWhite else DarkGrey
                )
            ) { Text("Add") }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DestructiveRed,
                    contentColor = if (isDarkMode) PureWhite else DarkGrey
                )
            ) { Text("Cancel") }
        }
    )
}
