package com.cse.tamagotchi.ui

import android.annotation.SuppressLint
import android.view.SoundEffectConstants
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import com.cse.tamagotchi.model.Task
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

    var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }
    var taskName by rememberSaveable { mutableStateOf("") }

    // Add Task Dialog
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
                            viewModel.addTask(taskName)
                            taskName = ""
                            showAddTaskDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )
                ) { Text("Add") }
            },
            dismissButton = {
                Button(
                    onClick = { showAddTaskDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DestructiveRed,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )
                ) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                contentColor = if (isDarkMode) PureWhite else DarkGrey
            ) { Icon(Icons.Default.Add, contentDescription = "Add Task") }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            CountdownTimer(nextResetTime = uiState.nextResetTime)
            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.tasks) { task ->
                    TaskItem(
                        task = task,
                        isDarkMode = isDarkMode,
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            viewModel.completeTask(task.id)
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, isDarkMode: Boolean, onClick: () -> Unit) {
    // Base and special colors
    val baseColor = if (isDarkMode) Color(0xAA1F1F1F) else Color(0xAAFFFFFF)
    val dailyColor = if (isDarkMode) Color(0xAA1F2E4F) else Color(0xAAE0F0FF) // blue tint
    val weeklyColor = if (isDarkMode) Color(0x552E4F1F) else Color(0x55CCFF80) // lime green tint

    val bubbleColor = when {
        task.isDaily -> dailyColor
        !task.isDaily -> weeklyColor
        else -> baseColor
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
            .background(color = bubbleColor, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        // Task title + checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isDarkMode) PureWhite else DarkGrey
            )

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isDarkMode) DarkModeGreen else LightModeGreen
                )
            )
        }

        Spacer(Modifier.height(4.dp))

        // Task description
        if (task.description.isNotBlank()) {
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isDarkMode) PureWhite.copy(alpha = 0.7f) else DarkGrey.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(6.dp))
        }

        // Coins, difficulty stars, and tag
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "💰 ${task.currencyReward}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDarkMode) PureWhite else DarkGrey
            )
            Spacer(Modifier.width(12.dp))

            val difficulty = (task.currencyReward / 10).coerceIn(1, 5)
            val stars = "⭐".repeat(difficulty) + "☆".repeat(5 - difficulty)
            Text(
                stars,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDarkMode) PureWhite else DarkGrey
            )

            // Task type label
            if (task.isDaily) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Daily",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkMode) Color(0xFF80D0FF) else Color(0xFF0077CC)
                )
            } else {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Weekly",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDarkMode) Color(0xFFFFB066) else Color(0xFFFF7700)
                )
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
