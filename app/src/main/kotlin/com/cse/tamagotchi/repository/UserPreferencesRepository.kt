package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val LAST_TASK_RESET_TIME = longPreferencesKey("last_task_reset_time")
        val LAST_WEEKLY_TASK_RESET_TIME = longPreferencesKey("last_weekly_task_reset_time")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.IS_DARK_MODE] ?: false }

    val lastTaskResetTime: Flow<Long> = context.dataStore.data
        .map { it[PreferencesKeys.LAST_TASK_RESET_TIME] ?: 0L }

    val lastWeeklyTaskResetTime: Flow<Long> = context.dataStore.data
        .map { it[PreferencesKeys.LAST_WEEKLY_TASK_RESET_TIME] ?: 0L }

    val musicVolume: Flow<Float> = context.dataStore.data
        .map { it[PreferencesKeys.MUSIC_VOLUME] ?: 0.25f } // default mid-level

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
        context.dataStore.edit { it.clear() }
    }
}
