package com.cse.tamagotchi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository

class TamagotchiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TamagotchiViewModel::class.java)) {
            val tamagotchiRepo = TamagotchiRepository(application)
            val storeDao = AppDatabase.getDatabase(application).storeItemDao()
            val storeRepo = StoreRepository(storeDao)
            @Suppress("UNCHECKED_CAST")
            return TamagotchiViewModel(tamagotchiRepo, storeRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
