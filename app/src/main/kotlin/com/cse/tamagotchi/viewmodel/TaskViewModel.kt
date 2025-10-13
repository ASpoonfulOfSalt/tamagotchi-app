package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.Task
import com.cse.tamagotchi.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(val repository: TaskRepository) : ViewModel() {
    val tasks: StateFlow<List<Task>> = repository.tasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.loadSampleTasks()
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            repository.completeTask(taskId)
        }
    }

}
