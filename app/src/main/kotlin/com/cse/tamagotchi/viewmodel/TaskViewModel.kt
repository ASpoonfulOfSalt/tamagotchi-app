package com.cse.tamagotchi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cse.tamagotchi.model.Task
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.TaskRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val nextResetTime: Long = 0L,
    val levelUpReward: Int = 0
)

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val tamagotchiRepository: TamagotchiRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    val tasks: StateFlow<List<Task>> = taskRepository.tasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            taskRepository.loadSampleTasks()
            checkAndResetTasks()
            tasks.collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }
            }
        }
    }

    private fun checkAndResetTasks() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val currentCal = Calendar.getInstance().apply { timeInMillis = currentTime }

            // Daily reset check
            val lastResetTime = userPreferencesRepository.lastTaskResetTime.first()
            val lastResetCal = Calendar.getInstance().apply { timeInMillis = lastResetTime }
            if (lastResetTime == 0L || lastResetCal.get(Calendar.DAY_OF_YEAR) != currentCal.get(Calendar.DAY_OF_YEAR)) {
                taskRepository.resetDailyTasks()
                userPreferencesRepository.updateLastTaskResetTime(currentTime)
            }

            // Weekly reset check
            val lastWeeklyResetTime = userPreferencesRepository.lastWeeklyTaskResetTime.first()
            val lastWeeklyResetCal = Calendar.getInstance().apply { timeInMillis = lastWeeklyResetTime }
            val isMonday = currentCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
            val isNewWeek = lastWeeklyResetTime == 0L || (isMonday && lastWeeklyResetCal.get(Calendar.WEEK_OF_YEAR) != currentCal.get(Calendar.WEEK_OF_YEAR))

            if (isNewWeek) {
                taskRepository.resetWeeklyTasks()
                userPreferencesRepository.updateLastWeeklyTaskResetTime(currentTime)
            }

            // Calculate next daily reset time
            val nextResetCal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            _uiState.update { it.copy(nextResetTime = nextResetCal.timeInMillis) }
        }
    }

    fun addTask(taskName: String) {
        viewModelScope.launch {
            val newTask = Task(title = taskName, isDaily = true, currencyReward = 10)
            taskRepository.addTask(newTask)
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            val completedTask = taskRepository.completeTask(taskId)
            if (completedTask != null) {
                val tamagotchi = tamagotchiRepository.tamagotchiFlow.first()
                val updatedTamagotchi = tamagotchi.addCurrency(completedTask.currencyReward)
                tamagotchiRepository.saveTamagotchi(updatedTamagotchi)

                val levelsGained = userPreferencesRepository.addXp(completedTask.xpReward)
                if (levelsGained > 0) {
                    val reward = levelsGained * 50
                    val newTamagotchi = tamagotchiRepository.tamagotchiFlow.first().addCurrency(reward)
                    tamagotchiRepository.saveTamagotchi(newTamagotchi)
                    _uiState.update { it.copy(levelUpReward = reward) }
                }
            }
        }
    }

    fun onLevelUpRewardShown() {
        _uiState.update { it.copy(levelUpReward = 0) }
    }
}
