package com.cse.tamagotchi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cse.tamagotchi.model.StoreItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StoreItem)

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getItemById(id: Int): StoreItem?

    @Query("SELECT * FROM inventory_items")
    fun getAllItems(): Flow<List<StoreItem>>

    @Query("DELETE FROM inventory_items")
    suspend fun clearAllInventory()

    @Transaction
    suspend fun upsert(item: StoreItem) {
        val existingItem = getItemById(item.id)
        if (existingItem == null) {
            insert(item.copy(quantity = 1))
        } else {
            insert(existingItem.copy(quantity = existingItem.quantity + 1))
        }
    }
}
