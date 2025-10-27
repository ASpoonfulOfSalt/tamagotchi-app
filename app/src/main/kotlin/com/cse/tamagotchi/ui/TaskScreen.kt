package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.DestructiveRed
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun TaskScreen(viewModel: TaskViewModel, isDarkMode: Boolean) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    // --- NEW: State for controlling the 'Add Task' dialog ---
    var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }
    var taskName by rememberSaveable { mutableStateOf("") }

    // --- NEW: The 'Add Task' Dialog ---
    if (showAddTaskDialog) {
        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Add a New Task") },
            text = {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskName.isNotBlank()) {
                            viewModel.addTask(taskName) // Call the ViewModel function
                            taskName = ""
                            showAddTaskDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showAddTaskDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DestructiveRed,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- NEW: Scaffold to hold the FAB ---
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                contentColor = if (isDarkMode) PureWhite else DarkGrey
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Use padding from the Scaffold
                .padding(16.dp)
        ) {
            CountdownTimer(nextResetTime = uiState.nextResetTime)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                // The user's provided code uses task.title, so we will use that.
                items(uiState.tasks) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                viewModel.completeTask(task.id)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(task.title, color = MaterialTheme.colorScheme.onBackground) // Assuming 'title' is the correct property name
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                viewModel.completeTask(task.id)
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = if (isDarkMode) DarkModeGreen else LightModeGreen
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountdownTimer(nextResetTime: Long) {
    var timeRemaining by remember { mutableStateOf("") }

    LaunchedEffect(key1 = nextResetTime) {
        while (true) {
            val remaining = nextResetTime - System.currentTimeMillis()
            if (remaining > 0) {
                val hours = remaining / (1000 * 60 * 60)
                val minutes = (remaining % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (remaining % (1000 * 60)) / 1000
                timeRemaining = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } else {
                timeRemaining = "00:00:00"
            }
            delay(1000)
        }
    }

    Text(
        text = "Tasks reset in: $timeRemaining",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    )
}
