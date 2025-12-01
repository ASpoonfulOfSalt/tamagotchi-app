package com.cse.tamagotchi.model

import kotlinx.serialization.Serializable

@Serializable
data class TriviaQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)
