package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val view = LocalView.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(tasks) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        viewModel.completeTask(task.id)
                    },
                horizontalArrangement = Arrangement.SpaceBetween
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
