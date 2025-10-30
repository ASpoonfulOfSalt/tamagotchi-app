package com.cse.tamagotchi.repository

import com.cse.tamagotchi.R
import com.cse.tamagotchi.data.StoreItemDao
import com.cse.tamagotchi.model.StoreItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreRepository(private val storeItemDao: StoreItemDao) {
    
    private val availableItems: List<StoreItem> = listOf(
        StoreItem(id = 1, name = "Apple", price = 10, iconRes = R.drawable.ic_food_apple),
        StoreItem(id = 2, name = "Book", price = 50, iconRes = R.drawable.ic_play_book),
        StoreItem(id = 3, name = "Ball", price = 30, iconRes = R.drawable.ic_play_ball),
        StoreItem(id = 4, name = "Water", price = 5, iconRes = R.drawable.ic_food_water),
        StoreItem(id = 5, name = "Cake", price = 100, iconRes = R.drawable.ic_food_cake)
    )

    fun getAvailableItems(): List<StoreItem> = availableItems

    fun getPurchasedItems(): Flow<List<StoreItem>> {
        val staticItemsById = availableItems.associateBy { it.id }

        return storeItemDao.getAllItems().map { dbItems ->
            dbItems.mapNotNull { dbItem ->
                staticItemsById[dbItem.id]?.copy(
                    quantity = dbItem.quantity
                )
            }
        }
    }

    suspend fun purchaseItem(item: StoreItem) {
        val existingItem = storeItemDao.getItemById(item.id)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            storeItemDao.update(updatedItem)
        } else {
            val newItem = item.copy(quantity = 1)
            storeItemDao.insert(newItem)
        }
    }

    suspend fun useItem(item: StoreItem) {
        if (item.quantity > 1) {
            storeItemDao.update(item.copy(quantity = item.quantity - 1))
        } else {
            storeItemDao.delete(item)
        }
    }

    suspend fun clearInventory() {
        storeItemDao.clearAllInventory()
    }
}
