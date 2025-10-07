package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_COINS = intPreferencesKey("user_coins")
    }

    val userCoins: Flow<Int> = context.dataStore.data
        .map {
            it[PreferencesKeys.USER_COINS] ?: 100 // Default to 100 coins
        }

    suspend fun updateUserCoins(newBalance: Int) {
        context.dataStore.edit {
            it[PreferencesKeys.USER_COINS] = newBalance
        }
    }
}
