package com.cse.tamagotchi.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "inventory_items")
data class StoreItem(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Int,
    @DrawableRes val iconRes: Int,
    val quantity: Int = 1,
    val description: String,
)
