// ui/TaskScreen.kt
package com.cse.tamagotchi.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Task

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onTaskClick(task.id) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(task.title)
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onTaskClick(task.id) }
                )
            }
        }
    }
}
