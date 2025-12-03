package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.repository.StatsRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StatsUiState(
    val installDate: Long = 0L,
    val totalTasksCompleted: Int = 0,
    val totalCoinsEarned: Int = 0,
    val totalMinutesUsed: Int = 0,
    val bestStreak: Int = 0,
    val daysUsed: Int = 0
)

class StatsViewModel(
    private val statsRepository: StatsRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            statsRepository.initializeInstallDateIfNeeded()
            statsRepository.recordDailyUsageIfNewDay()

            combine(
                listOf(
                    statsRepository.installDate,
                    statsRepository.totalTasksCompleted,
                    statsRepository.totalCoinsEarned,
                    statsRepository.totalAppMinutes,
                    statsRepository.bestStreak,
                    statsRepository.daysUsed
                )
            ) { values ->
                StatsUiState(
                    installDate = values[0] as Long,
                    totalTasksCompleted = values[1] as Int,
                    totalCoinsEarned = values[2] as Int,
                    totalMinutesUsed = values[3] as Int,
                    bestStreak = values[4] as Int,
                    daysUsed = values[5] as Int
                )
            }.collect { _uiState.value = it }

        }
    }

    fun updateBestStreak(currentStreak: Int) {
        viewModelScope.launch {
            statsRepository.updateBestStreak(currentStreak)
        }
    }

    fun addUsageMinutes(minutes: Int) {
        viewModelScope.launch {
            statsRepository.recordAppUsage(minutes)
        }
    }

    fun onTaskCompleted(reward: Int) {
        viewModelScope.launch {
            statsRepository.recordTaskCompleted()
            statsRepository.recordCoinsEarned(reward)
        }
    }
}
