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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

private val Context.tamaDataStore: DataStore<Preferences> by preferencesDataStore("tamagotchi_prefs")

class TamagotchiRepository(private val context: Context) {

    private object Keys {
        val TAMA_JSON = stringPreferencesKey("tama_json")
    }

    // Config: decay rates (points per hour)
    private val hungerDecayPerHour = 5.0    // hunger drops 5 points/hour
    private val waterDecayPerHour = 4.0
    private val happinessDecayPerHour = 2.0

    val tamagotchiFlow: Flow<Tamagotchi> = context.tamaDataStore.data.map { prefs ->
        val json = prefs[Keys.TAMA_JSON]
        val raw = json?.let {
            try { Json.decodeFromString<Tamagotchi>(it) } catch (_: Exception) { Tamagotchi() }
        } ?: Tamagotchi()

        // compute decay since lastUpdatedMillis
        val now = System.currentTimeMillis()
        val elapsedMs = (now - raw.lastUpdatedMillis).coerceAtLeast(0L)
        val hours = elapsedMs / (1000.0 * 60.0 * 60.0)

        if (hours <= 0.0) {
            // Nothing to decay
            raw
        } else {
            val hungerDelta = (hours * hungerDecayPerHour).roundToInt()
            val waterDelta = (hours * waterDecayPerHour).roundToInt()
            val happinessDelta = (hours * happinessDecayPerHour).roundToInt()

            raw.applyDecay(hungerDelta, waterDelta, happinessDelta, now)
        }
    }

    suspend fun saveTamagotchi(t: Tamagotchi) {
        context.tamaDataStore.edit { prefs ->
            prefs[Keys.TAMA_JSON] = Json.encodeToString(t)
        }
    }

    suspend fun resetTamagotchiToDefault() {
        context.tamaDataStore.edit { prefs -> prefs.remove(Keys.TAMA_JSON) }
    }
}
