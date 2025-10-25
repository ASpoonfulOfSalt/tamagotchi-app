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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CountdownTimer(nextResetTime = uiState.nextResetTime)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp)
        ) {
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
                    Text(task.title)
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { 
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            viewModel.completeTask(task.id) 
                        }
                    )
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
        modifier = Modifier.fillMaxWidth()
    )
}
