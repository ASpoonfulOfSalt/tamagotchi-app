package com.cse.tamagotchi.repository

import com.cse.tamagotchi.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskRepository {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    fun completeTask(taskId: String) {
        _tasks.value = _tasks.value.map { task ->
           if (task.id == taskId) task.copy(isCompleted = true) else task
        }
    }

    fun loadSampleTasks() {
        // TODO: Replace with true Repository behaviour.
        val sampleTasks = listOf(
            Task(title = "Drink Water", isDaily = true),
            Task(title = "Go for a Walk", isDaily = true),
            Task(title = "Study Kotlin", isDaily = true),
            Task(title = "Weekly Clean Room", isDaily = false)
        )
        _tasks.value = sampleTasks
    }
}