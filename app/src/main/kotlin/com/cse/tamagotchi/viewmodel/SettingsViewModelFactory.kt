package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.TaskRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository

class SettingsViewModelFactory(
    private val userPrefs: UserPreferencesRepository,
    private val database: AppDatabase,
    private val taskRepository: TaskRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userPrefs, database, taskRepository, tamagotchiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}