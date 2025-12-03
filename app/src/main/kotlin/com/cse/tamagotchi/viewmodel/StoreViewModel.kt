package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.Hat
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
    val purchaseMessage: String? = null,
    val currentHat: Hat? = null
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
        loadTamagotchiData()
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

    private fun loadTamagotchiData() {
        viewModelScope.launch {
            tamagotchiRepository.tamagotchiFlow.collect { tamagotchi ->
                _uiState.update { 
                    it.copy(
                        userCoins = tamagotchi.currency,
                        currentHat = tamagotchi.currentHat
                    ) 
                }
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

    fun equipHat(item: StoreItem) {
        val hat = mapItemToHat(item)
        if (hat != null) {
            viewModelScope.launch {
                val tamagotchi = tamagotchiRepository.tamagotchiFlow.first()
                // Toggle: If already wearing this hat, unequip it. Otherwise, equip it.
                val newTamagotchi = if (tamagotchi.currentHat == hat) {
                    tamagotchi.unequipHat()
                } else {
                    tamagotchi.equipHat(hat)
                }
                tamagotchiRepository.saveTamagotchi(newTamagotchi)
                
                _uiState.update { 
                    it.copy(purchaseMessage = if (newTamagotchi.currentHat == hat) "Equipped ${item.name}!" else "Unequipped ${item.name}!")
                }
            }
        }
    }

    fun getHatFromItem(item: StoreItem): Hat? {
        return mapItemToHat(item)
    }

    private fun mapItemToHat(item: StoreItem): Hat? {
        return when (item.name) {
            "Baseball Cap" -> Hat.BASEBALL
            "Beanie" -> Hat.BEANIE
            "Bucket Hat" -> Hat.BUCKET
            "Cowboy Hat" -> Hat.COWBOY
            "Party Hat" -> Hat.PARTY
            "Top Hat" -> Hat.TOP
            else -> null
        }
    }

    fun onPurchaseMessageShown() {
        _uiState.update { it.copy(purchaseMessage = null) }
    }
}
