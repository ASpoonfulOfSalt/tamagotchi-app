package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlin.math.max

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore("stats_prefs")

class StatsRepository(private val context: Context) {

    private object Keys {
        val INSTALL_DATE = longPreferencesKey("install_date")
        val TOTAL_TASKS_COMPLETED = intPreferencesKey("total_tasks_completed")
        val TOTAL_COINS_EARNED = intPreferencesKey("total_coins_earned")
        val TOTAL_APP_MINUTES = intPreferencesKey("total_app_minutes")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val DAYS_USED = intPreferencesKey("days_used")
        val LAST_OPENED_DAY = longPreferencesKey("last_opened_day")
    }

    val installDate: Flow<Long> = context.statsDataStore.data
        .map { it[Keys.INSTALL_DATE] ?: 0L }

    val totalTasksCompleted: Flow<Int> = context.statsDataStore.data
        .map { it[Keys.TOTAL_TASKS_COMPLETED] ?: 0 }

    val totalCoinsEarned: Flow<Int> = context.statsDataStore.data
        .map { it[Keys.TOTAL_COINS_EARNED] ?: 0 }

    val totalAppMinutes: Flow<Int> = context.statsDataStore.data
        .map { it[Keys.TOTAL_APP_MINUTES] ?: 0 }

    val bestStreak: Flow<Int> = context.statsDataStore.data
        .map { it[Keys.BEST_STREAK] ?: 0 }

    val daysUsed: Flow<Int> = context.statsDataStore.data
        .map { it[Keys.DAYS_USED] ?: 0 }


    suspend fun initializeInstallDateIfNeeded() {
        val current = installDate.first()
        if (current == 0L) {
            context.statsDataStore.edit {
                it[Keys.INSTALL_DATE] = System.currentTimeMillis()
            }
        }
    }

    suspend fun recordTaskCompleted() {
        context.statsDataStore.edit {
            val current = it[Keys.TOTAL_TASKS_COMPLETED] ?: 0
            it[Keys.TOTAL_TASKS_COMPLETED] = current + 1
        }
    }

    suspend fun recordCoinsEarned(amount: Int) {
        context.statsDataStore.edit {
            val current = it[Keys.TOTAL_COINS_EARNED] ?: 0
            it[Keys.TOTAL_COINS_EARNED] = current + amount
        }
    }

    suspend fun recordAppUsage(minutes: Int) {
        context.statsDataStore.edit {
            val current = it[Keys.TOTAL_APP_MINUTES] ?: 0
            it[Keys.TOTAL_APP_MINUTES] = current + minutes
        }
    }

    suspend fun updateBestStreak(currentStreak: Int) {
        context.statsDataStore.edit {
            val prev = it[Keys.BEST_STREAK] ?: 0
            it[Keys.BEST_STREAK] = max(prev, currentStreak)
        }
    }

    suspend fun recordDailyUsageIfNewDay() {
        val todayStart = getDayStartMillis(System.currentTimeMillis())
        val lastOpened = context.statsDataStore.data.first()[Keys.LAST_OPENED_DAY] ?: 0L

        if (lastOpened != todayStart) {
            context.statsDataStore.edit {
                val days = it[Keys.DAYS_USED] ?: 0
                it[Keys.DAYS_USED] = days + 1
                it[Keys.LAST_OPENED_DAY] = todayStart
            }
        }
    }

    private fun getDayStartMillis(time: Long): Long {
        val cal = java.util.Calendar.getInstance().apply {
            timeInMillis = time
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }
}
