package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StoreUiState(
    val items: List<StoreItem> = emptyList(),
    val userCoins: Int = 0,
    val userInventory: List<StoreItem> = emptyList(),
    val purchaseMessage: String? = null
)

class StoreViewModel(
    private val storeRepository: StoreRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    init {
        loadStoreItems()
        loadUserInventory()
        loadUserCoins()
    }

    private fun loadStoreItems() {
        viewModelScope.launch {
            val items = storeRepository.getAvailableItems()
            _uiState.update { it.copy(items = items) }
        }
    }

    private fun loadUserInventory() {
        viewModelScope.launch {
            storeRepository.getPurchasedItems().collect { inventory ->
                _uiState.update { it.copy(userInventory = inventory) }
            }
        }
    }

    private fun loadUserCoins() {
        viewModelScope.launch {
            tamagotchiRepository.tamagotchiFlow.collect { tamagotchi ->
                _uiState.update { it.copy(userCoins = tamagotchi.currency) }
            }
        }
    }

    fun purchaseItem(item: StoreItem) {
        viewModelScope.launch {
            val tamagotchi = tamagotchiRepository.tamagotchiFlow.first()
            if (tamagotchi.currency >= item.price) {
                val newTamagotchi = tamagotchi.copy(currency = tamagotchi.currency - item.price)
                tamagotchiRepository.saveTamagotchi(newTamagotchi)
                storeRepository.purchaseItem(item)

                _uiState.update {
                    it.copy(purchaseMessage = "${item.name} purchased!")
                }
            } else {
                _uiState.update {
                    it.copy(purchaseMessage = "Not enough coins to buy ${item.name}.")
                }
            }
        }
    }

    fun onPurchaseMessageShown() {
        _uiState.update { it.copy(purchaseMessage = null) }
    }
}
