package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StatsUiState(
    val installDate: Long = 0L,
    val totalTasksCompleted: Int = 0,
    val totalCoinsEarned: Int = 0,
    val totalMinutesUsed: Int = 0,
    val bestStreak: Int = 0,
    val daysUsed: Int = 0,

    val currentStreak: Int = 0,
    val level: Int = 1,
    val xp: Int = 0,
    val hatCount: Int = 0
)

private data class BaseStats(
    val installDate: Long,
    val totalTasksCompleted: Int,
    val totalCoinsEarned: Int,
    val totalMinutesUsed: Int,
    val bestStreak: Int,
    val daysUsed: Int,
    val level: Int,
    val xp: Int
)

class StatsViewModel(
    private val statsRepository: StatsRepository,
    private val userPrefs: UserPreferencesRepository,
    private val storeRepository: StoreRepository,
    private val tamagotchiRepository: TamagotchiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            statsRepository.initializeInstallDateIfNeeded()
            statsRepository.recordDailyUsageIfNewDay()

            // FIXED: combine using array
            val baseFlow = combine(
                statsRepository.installDate,
                statsRepository.totalTasksCompleted,
                statsRepository.totalCoinsEarned,
                statsRepository.totalAppMinutes,
                statsRepository.bestStreak,
                statsRepository.daysUsed,
                userPrefs.userLevel,
                userPrefs.userXp
            ) { values ->

                BaseStats(
                    installDate = values[0] as Long,
                    totalTasksCompleted = values[1] as Int,
                    totalCoinsEarned = values[2] as Int,
                    totalMinutesUsed = values[3] as Int,
                    bestStreak = values[4] as Int,
                    daysUsed = values[5] as Int,
                    level = values[6] as Int,
                    xp = values[7] as Int
                )
            }

            combine(
                baseFlow,
                storeRepository.getPurchasedItems(),
                tamagotchiRepository.tamagotchiFlow
            ) { base, items, tama ->

                val hatCount = items.count { it.isHat() && it.quantity > 0 }

                StatsUiState(
                    installDate = base.installDate,
                    totalTasksCompleted = base.totalTasksCompleted,
                    totalCoinsEarned = base.totalCoinsEarned,
                    totalMinutesUsed = base.totalMinutesUsed,
                    bestStreak = base.bestStreak,
                    daysUsed = base.daysUsed,
                    level = base.level,
                    xp = base.xp,
                    hatCount = hatCount,
                    currentStreak = tama.streakCount
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun updateBestStreak(current: Int) {
        viewModelScope.launch { statsRepository.updateBestStreak(current) }
    }

    fun onTaskCompleted(reward: Int) {
        viewModelScope.launch {
            statsRepository.recordTaskCompleted()
            statsRepository.recordCoinsEarned(reward)
        }
    }

    fun addUsageMinutes(minutes: Int) {
        viewModelScope.launch { statsRepository.recordAppUsage(minutes) }
    }
}

private fun com.cse.tamagotchi.model.StoreItem.isHat(): Boolean =
    this.id >= 10
