package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val LAST_TASK_RESET_TIME = longPreferencesKey("last_task_reset_time")
        val LAST_WEEKLY_TASK_RESET_TIME = longPreferencesKey("last_weekly_task_reset_time")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
        val USER_XP = intPreferencesKey("user_xp")
        val USER_LEVEL = intPreferencesKey("user_level")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.HAS_SEEN_ONBOARDING] ?: false }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.IS_DARK_MODE] ?: false }

    val lastTaskResetTime: Flow<Long> = context.dataStore.data
        .map { it[PreferencesKeys.LAST_TASK_RESET_TIME] ?: 0L }

    val lastWeeklyTaskResetTime: Flow<Long> = context.dataStore.data
        .map { it[PreferencesKeys.LAST_WEEKLY_TASK_RESET_TIME] ?: 0L }

    val musicVolume: Flow<Float> = context.dataStore.data
        .map { it[PreferencesKeys.MUSIC_VOLUME] ?: 0.25f } // default mid-level

    val userXp: Flow<Int> = context.dataStore.data
        .map { it[PreferencesKeys.USER_XP] ?: 0 }

    val userLevel: Flow<Int> = context.dataStore.data
        .map { it[PreferencesKeys.USER_LEVEL] ?: 1 }

    suspend fun addXp(xp: Int): Int {
        var levelsGained = 0
        context.dataStore.edit {
            val currentLevel = it[PreferencesKeys.USER_LEVEL] ?: 1
            var currentXp = it[PreferencesKeys.USER_XP] ?: 0
            currentXp += xp

            var xpForNextLevel = 100 * currentLevel
            var newLevel = currentLevel

            while (currentXp >= xpForNextLevel) {
                currentXp -= xpForNextLevel
                newLevel++
                xpForNextLevel = 100 * newLevel
            }

            if (newLevel > currentLevel) {
                levelsGained = newLevel - currentLevel
                it[PreferencesKeys.USER_LEVEL] = newLevel
            }

            it[PreferencesKeys.USER_XP] = currentXp
        }

        return levelsGained
    }

    suspend fun updateMusicVolume(volume: Float) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.MUSIC_VOLUME] = volume.coerceIn(0f, 1f)
        }
    }

    suspend fun updateLastTaskResetTime(time: Long) {
        context.dataStore.edit {
            it[PreferencesKeys.LAST_TASK_RESET_TIME] = time
        }
    }

    suspend fun updateLastWeeklyTaskResetTime(time: Long) {
        context.dataStore.edit {
            it[PreferencesKeys.LAST_WEEKLY_TASK_RESET_TIME] = time
        }
    }

    suspend fun toggleTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_DARK_MODE] = isDark
        }
    }

    suspend fun clearAllData() {
        val isDarkMode = isDarkMode.first()
        val musicVolume = musicVolume.first()

        context.dataStore.edit {
            it.clear()
            it[PreferencesKeys.IS_DARK_MODE] = isDarkMode
            it[PreferencesKeys.MUSIC_VOLUME] = musicVolume
        }
    }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.HAS_SEEN_ONBOARDING] = true
        }
    }
}