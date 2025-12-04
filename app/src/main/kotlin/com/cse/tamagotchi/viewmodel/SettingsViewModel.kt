package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.audio.BackgroundMusicManager
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.model.Tamagotchi
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.TaskRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val userPrefs: UserPreferencesRepository,
    private val database: AppDatabase,
    private val taskRepository: TaskRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModel() {

    val isDarkMode = userPrefs.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val musicVolume = userPrefs.musicVolume
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.25f)

    fun updateMusicVolume(volume: Float) {
        viewModelScope.launch {
            userPrefs.updateMusicVolume(volume)
            BackgroundMusicManager.setVolume(volume)
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPrefs.toggleTheme(enabled)
        }
    }

    fun resetPetStats() {
        viewModelScope.launch {
            tamagotchiRepository.saveTamagotchi(Tamagotchi())
        }
    }

    fun resetAppData() {
        viewModelScope.launch {
            // Clear user preferences
            userPrefs.clearAllData()

            // Clear Room data
            withContext(Dispatchers.IO) {
                database.storeItemDao().clearAllInventory()
            }

            // Reset tasks
            taskRepository.resetDailyTasks()
            taskRepository.resetWeeklyTasks()

            // Reset pet
            resetPetStats()
        }
    }

    fun makeMeABajillionaire() {
        viewModelScope.launch {
            val tamagotchi = tamagotchiRepository.tamagotchiFlow.stateIn(
                viewModelScope, SharingStarted.Eagerly, null
            ).value ?: Tamagotchi()

            // Pick a value that is big but safe for Int (Room stores currency)
            val million = 1_000_000
            val newTamagotchi = tamagotchi.addCurrency(million)

            tamagotchiRepository.saveTamagotchi(newTamagotchi)
        }
    }

}
