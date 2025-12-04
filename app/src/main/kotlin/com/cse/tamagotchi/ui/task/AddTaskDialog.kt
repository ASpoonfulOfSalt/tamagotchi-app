package com.cse.tamagotchi.task

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add a New Task") },
        text = {
            OutlinedTextField(
                value = taskName,
                onValueChange = onNameChanged,
                label = { Text("Task Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
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
