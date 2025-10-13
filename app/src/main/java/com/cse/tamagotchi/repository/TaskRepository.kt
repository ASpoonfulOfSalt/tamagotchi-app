package com.cse.tamagotchi.repository

import com.cse.tamagotchi.data.TaskDao
import com.cse.tamagotchi.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TaskRepository(private val taskDao: TaskDao) {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun completeTask(taskId: String) {
        val task = tasks.first().find { it.id == taskId }
        if (task != null) {
            taskDao.updateTask(task.copy(isCompleted = true))
        }
    }

    suspend fun resetTasks() {
        taskDao.resetAllTasks()
    }

    suspend fun loadSampleTasks() {
        if (tasks.first().isEmpty()) {
            val sampleTasks = listOf(
                Task(title = "Drink Water", isDaily = true),
                Task(title = "Go for a Walk", isDaily = true),
                Task(title = "Study Kotlin", isDaily = true),
                Task(title = "Weekly Clean Room", isDaily = false)
            )
            taskDao.insertAll(sampleTasks)
        }
    }
}
