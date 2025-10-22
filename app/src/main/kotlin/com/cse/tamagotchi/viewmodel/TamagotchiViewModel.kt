package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.Tamagotchi
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TamagotchiViewModel(
    private val repo: TamagotchiRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TamagotchiUiState())
    val uiState: StateFlow<TamagotchiUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repo.tamagotchiFlow,
                storeRepository.getPurchasedItems()
            ) { tamagotchi, inventory ->
                tamagotchi to inventory
            }.collect { (tamagotchi, inventory) ->
                _uiState.update { currentState ->
                    currentState.copy(
                        tamagotchi = tamagotchi,
                        inventory = inventory,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun feedPet() = useItem("Apple") { it.feed() }

    fun hydratePet() = useItem("Water") { it.hydrate() }

    fun playPet() = useItem("Ball") { it.play() }

    fun renamePet(newName: String) = viewModelScope.launch {
        val current = _uiState.value.tamagotchi
        val newT = current.copy(name = newName, lastUpdatedMillis = System.currentTimeMillis())
        repo.saveTamagotchi(newT)
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun useItem(itemName: String, transform: (Tamagotchi) -> Tamagotchi) {
        viewModelScope.launch {
            if (_uiState.value.isLoading) return@launch

            val item = _uiState.value.inventory.find { it.name == itemName }
            if (item != null) {
                storeRepository.useItem(item)
                val current = _uiState.value.tamagotchi
                val newT = transform(current)
                repo.saveTamagotchi(newT)
            } else {
                _uiState.update { it.copy(userMessage = "You don't have any ${itemName}s left!") }
            }
        }
    }
}
