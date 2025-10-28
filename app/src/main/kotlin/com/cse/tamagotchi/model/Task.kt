package com.cse.tamagotchi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "is_daily")
    val isDaily: Boolean = true,
    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,
    @ColumnInfo(name = "currency_reward")
    val currencyReward: Int = 10,
    @ColumnInfo(name = "xp_reward")
    val xpReward: Int = 20
)
