package com.cse.tamagotchi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Task

// TODO: IMPORTANT -  CONTINUE WORKING ON TASKSCREEN

@Composable
fun TaskScreen(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(tasks) { task ->
            Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onTaskClick(task.id) }, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(task.title)
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onTaskClick(task.id) }
                )
            }
        }
    }
}
