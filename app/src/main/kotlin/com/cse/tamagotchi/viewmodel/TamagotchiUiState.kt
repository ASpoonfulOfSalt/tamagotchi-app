package com.cse.tamagotchi.viewmodel

import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.model.Tamagotchi

data class TamagotchiUiState(
    val tamagotchi: Tamagotchi = Tamagotchi(),
    val inventory: List<StoreItem> = emptyList(),
    val isLoading: Boolean = true,
    val userMessage: String? = null,
    val speechBubbleMessage: String? = null
)
