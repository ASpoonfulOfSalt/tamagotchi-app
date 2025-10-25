package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository

class StoreViewModelFactory(
    private val storeRepository: StoreRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoreViewModel(storeRepository, tamagotchiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
