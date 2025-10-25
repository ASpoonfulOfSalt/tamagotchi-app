package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository

class TamagotchiViewModelFactory(
    private val tamagotchiRepository: TamagotchiRepository,
    private val storeRepository: StoreRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TamagotchiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TamagotchiViewModel(tamagotchiRepository, storeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
