package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.repository.StatsRepository
import com.cse.tamagotchi.repository.TaskRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository

class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val tamagotchiRepository: TamagotchiRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val statsRepository: StatsRepository // <- new param
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskRepository, tamagotchiRepository, userPreferencesRepository, statsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
