package com.cse.tamagotchi.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cse.tamagotchi.model.Tamagotchi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

// DataStore delegate
private val Context.tamaDataStore: DataStore<Preferences> by preferencesDataStore("tamagotchi_prefs")

class TamagotchiRepository(private val context: Context) {

    private object Keys {
        val TAMA_JSON = stringPreferencesKey("tama_json")
    }

    // Decay rates per hour
    private val hungerDecayPerHour = 5.0
    private val waterDecayPerHour = 4.0
    private val happinessDecayPerHour = 2.0

    // Flow to observe Tamagotchi state
    val tamagotchiFlow: Flow<Tamagotchi> = context.tamaDataStore.data.map { prefs ->
        val json = prefs[Keys.TAMA_JSON]

        // Use explicit serializer for decoding
        val raw = json?.let {
            try {
                Json.decodeFromString(Tamagotchi.serializer(), it)
            } catch (_: Exception) {
                Tamagotchi()
            }
        } ?: Tamagotchi()

        // Compute decay based on elapsed time
        val now = System.currentTimeMillis()
        val elapsedMs = (now - raw.lastUpdatedMillis).coerceAtLeast(0L)
        val hours = elapsedMs / (1000.0 * 60.0 * 60.0)

        if (hours <= 0.0) {
            raw
        } else {
            val hungerDelta = (hours * hungerDecayPerHour).roundToInt()
            val waterDelta = (hours * waterDecayPerHour).roundToInt()
            val happinessDelta = (hours * happinessDecayPerHour).roundToInt()

            raw.applyDecay(hungerDelta, waterDelta, happinessDelta, now)
        }
    }

    // Save Tamagotchi state using explicit serializer
    suspend fun saveTamagotchi(t: Tamagotchi) {
        context.tamaDataStore.edit { prefs ->
            prefs[Keys.TAMA_JSON] = Json.encodeToString(Tamagotchi.serializer(), t)
        }
    }

    // Reset to default state
    suspend fun resetTamagotchiToDefault() {
        context.tamaDataStore.edit { prefs ->
            prefs.remove(Keys.TAMA_JSON)
        }
    }
}
