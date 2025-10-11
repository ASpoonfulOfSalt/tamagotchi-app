package com.cse.tamagotchi.model

// TODO: Unused
data class Pet(
    val name: String,
    var hunger: Int = 50,
    var water: Int = 50,
    var happiness: Int = 50
) {
    fun feed(amount: Int) {
        hunger = (hunger + amount).coerceAtMost(100)
    }

    fun giveWater(amount: Int) {
        water = (water + amount).coerceAtMost(100)
    }

    fun play(amount: Int) {
        happiness = (happiness + amount).coerceAtMost(100)
    }
}
