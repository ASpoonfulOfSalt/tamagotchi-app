package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import com.cse.tamagotchi.model.Task
import com.cse.tamagotchi.repository.TaskRepository
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()
    val tasks: StateFlow<List<Task>> = repository.tasks

    init {
        repository.loadSampleTasks()
    }

    fun completeTask(taskId: String) {
        repository.completeTask(taskId)
    }

    fun addTask(task: Task) {
        repository.addTask(task)
    }
}