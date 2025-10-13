package com.cse.tamagotchi.model

import kotlinx.serialization.Serializable

@Serializable
data class Tamagotchi(
    val name: String = "Rico",
    val hunger: Int = 80,      // 0..100, higher = more fed
    val water: Int = 80,       // 0..100
    val happiness: Int = 80,   // 0..100
    val lastUpdatedMillis: Long = System.currentTimeMillis()
) {
    fun feed(amount: Int = 15): Tamagotchi =
        copy(hunger = (hunger + amount).coerceAtMost(100), lastUpdatedMillis = System.currentTimeMillis())

    fun hydrate(amount: Int = 15): Tamagotchi =
        copy(water = (water + amount).coerceAtMost(100), lastUpdatedMillis = System.currentTimeMillis())

    fun play(amount: Int = 15): Tamagotchi =
        copy(happiness = (happiness + amount).coerceAtMost(100), lastUpdatedMillis = System.currentTimeMillis())

    fun applyDecay(hungerDelta: Int, waterDelta: Int, happinessDelta: Int, nowMillis: Long = System.currentTimeMillis()): Tamagotchi {
        val newH = (hunger - hungerDelta).coerceIn(0, 100)
        val newW = (water - waterDelta).coerceIn(0, 100)
        val newS = (happiness - happinessDelta).coerceIn(0, 100)
        return copy(hunger = newH, water = newW, happiness = newS, lastUpdatedMillis = nowMillis)
    }
}
