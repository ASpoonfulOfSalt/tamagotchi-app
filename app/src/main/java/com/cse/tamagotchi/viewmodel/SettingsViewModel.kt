package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPrefs: UserPreferencesRepository,
    private val database: AppDatabase
) : ViewModel() {

    val isDarkMode = userPrefs.isDarkMode // unresolved reference "isDarkMode"
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPrefs.toggleTheme(enabled)
        }
    }

    fun resetAppData() {
        viewModelScope.launch {
            // Clear user preferences
            userPrefs.clearAllData()

            // Clear Room data
            database.clearAllTables()
        }
    }
}
