package com.cse.tamagotchi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.Tamagotchi
import com.cse.tamagotchi.repository.TamagotchiRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TamagotchiViewModel(private val repo: TamagotchiRepository) : ViewModel() {
    val tamagotchi: StateFlow<Tamagotchi> = repo.tamagotchiFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Tamagotchi())

    fun feedPet() = modify { it.feed() }
    fun hydratePet() = modify { it.hydrate() }
    fun playPet() = modify { it.play() }
    fun renamePet(newName: String) = modify { it.copy(name = newName, lastUpdatedMillis = System.currentTimeMillis()) }

    private fun modify(transform: (Tamagotchi) -> Tamagotchi) {
        viewModelScope.launch {
            val current = tamagotchi.value
            val newT = transform(current)
            repo.saveTamagotchi(newT)
        }
    }
}
