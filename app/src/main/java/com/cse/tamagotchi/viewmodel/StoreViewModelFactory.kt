package com.cse.tamagotchi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository

class StoreViewModelFactory(
    private val application: Application,
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            val storeItemDao = database.storeItemDao()
            val storeRepository = StoreRepository(storeItemDao)
            val userPreferencesRepository = UserPreferencesRepository(application)
            @Suppress("UNCHECKED_CAST")
            return StoreViewModel(storeRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
