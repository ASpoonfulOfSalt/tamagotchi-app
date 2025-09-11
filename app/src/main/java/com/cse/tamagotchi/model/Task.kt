package com.cse.tamagotchi.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val isDaily: Boolean = true,
    var isCompleted: Boolean = false
)
