package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_COINS = intPreferencesKey("user_coins")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val userCoins: Flow<Int> = context.dataStore.data
        .map {
            it[PreferencesKeys.USER_COINS] ?: 100 // Default to 100 coins
        }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.IS_DARK_MODE] ?: false }

    suspend fun updateUserCoins(newBalance: Int) {
        context.dataStore.edit {
            it[PreferencesKeys.USER_COINS] = newBalance
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
