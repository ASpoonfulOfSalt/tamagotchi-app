package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.repository.StatsRepository
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository

class StatsViewModelFactory(
    private val statsRepository: StatsRepository,
    private val userPrefs: UserPreferencesRepository,
    private val storeRepository: StoreRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(
                statsRepository = statsRepository,
                userPrefs = userPrefs,
                storeRepository = storeRepository,
                tamagotchiRepository = tamagotchiRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
