// viewmodel/HomeViewModel.kt
package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _coins = MutableStateFlow(0)
    val coins: StateFlow<Int> = _coins

    fun addCoins(amount: Int) {
        _coins.value += amount
    }
}
