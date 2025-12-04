package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.task.AddTaskDialog
import com.cse.tamagotchi.task.CountdownTimer
import com.cse.tamagotchi.task.TaskItem
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel, isDarkMode: Boolean) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    var showAddTaskDialog by rememberSaveable { mutableStateOf(false) }
    var taskName by rememberSaveable { mutableStateOf("") }

    // === Add Task Dialog ===
    AddTaskDialog(
        isVisible = showAddTaskDialog,
        taskName = taskName,
        isDarkMode = isDarkMode,
        onNameChanged = { taskName = it },
        onDismiss = { showAddTaskDialog = false },
        onConfirm = {
            if (taskName.isNotBlank()) {
                viewModel.addTask(taskName)
                taskName = ""
                showAddTaskDialog = false
            }
        }
    )

    // === Main Layout ===
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            CountdownTimer(nextResetTime = uiState.nextResetTime)
            Spacer(Modifier.height(16.dp))

            val sortedTasks = uiState.tasks.sortedBy { it.isCompleted }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(sortedTasks) { task ->
                    TaskItem(
                        task = task,
                        isDarkMode = isDarkMode,
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            if (!task.isCompleted) {
                                viewModel.completeTask(task.id)
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
