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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.Calendar

class TamagotchiViewModel(
    private val repo: TamagotchiRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TamagotchiUiState())
    val uiState: StateFlow<TamagotchiUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Check and update the streak first
            checkAndUpdateStreak()

            // Then, continue setting up the UI state listeners
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

    private fun checkAndUpdateStreak() = viewModelScope.launch {
        // Fetch the most recent Tamagotchi state once
        val tamagotchi = repo.tamagotchiFlow.first()

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayMillis = today.timeInMillis

        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val yesterdayMillis = yesterday.timeInMillis

        val newStreak: Int
        var newT = tamagotchi

        when (tamagotchi.lastDayOpened) {
            todayMillis -> {
                // App already opened today, do nothing to the streak.
                return@launch
            }
            yesterdayMillis -> {
                // Opened yesterday, increment streak.
                newStreak = tamagotchi.streakCount + 1
                showSpeechBubble("Streak +1!")
            }
            else -> {
                // Missed a day, reset streak to 1.
                newStreak = 1
                _uiState.update { it.copy(userMessage = "Welcome back! Your new streak starts now.") }
            }
        }

        newT = tamagotchi.copy(streakCount = newStreak, lastDayOpened = todayMillis)
        repo.saveTamagotchi(newT)
    }

    fun feedPet() = useItem("Apple", "Yummy!") { it.feed() }

    fun hydratePet() = useItem("Water", "Refreshing!") { it.hydrate() }

    fun playPet() = useItem("Ball", "So fun!") { it.play() }

    fun renamePet(newName: String) = viewModelScope.launch {
        val current = _uiState.value.tamagotchi
        val newT = current.copy(name = newName, lastUpdatedMillis = System.currentTimeMillis())
        repo.saveTamagotchi(newT)
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    private fun showSpeechBubble(message: String) {
        _uiState.update { it.copy(speechBubbleMessage = message) }
        viewModelScope.launch {
            delay(2500) // The bubble will be visible for 2.5 seconds
            _uiState.update { it.copy(speechBubbleMessage = null) }
        }
    }

    private fun useItem(itemName: String, speechMessage: String, transform: (Tamagotchi) -> Tamagotchi) {
        viewModelScope.launch {
            if (_uiState.value.isLoading) return@launch

            val item = _uiState.value.inventory.find { it.name == itemName }
            if (item != null) {
                storeRepository.useItem(item)
                val current = _uiState.value.tamagotchi
                val newT = transform(current)
                repo.saveTamagotchi(newT)
                showSpeechBubble(speechMessage) // Show the speech bubble on success
            } else {
                _uiState.update { it.copy(userMessage = "You don't have any ${itemName}s left!") }
            }
        }
    }
}
