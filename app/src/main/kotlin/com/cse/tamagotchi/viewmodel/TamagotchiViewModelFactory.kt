package com.cse.tamagotchi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cse.tamagotchi.repository.TamagotchiRepository

class TamagotchiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TamagotchiViewModel::class.java)) {
            val repo = TamagotchiRepository(application)
            @Suppress("UNCHECKED_CAST")
            return TamagotchiViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
