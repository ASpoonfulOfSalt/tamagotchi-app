package com.cse.tamagotchi.repository

import com.cse.tamagotchi.R
import com.cse.tamagotchi.data.StoreItemDao
import com.cse.tamagotchi.model.StoreItem
import kotlinx.coroutines.flow.Flow

class StoreRepository(private val storeItemDao: StoreItemDao) {
    /*
    Static list of items available in the store
     */
    fun getAvailableItems(): List<StoreItem> {
        return listOf(
            StoreItem(id = 1, name = "Apple", price = 10, iconRes = R.drawable.ic_food_apple),
            StoreItem(id = 2, name = "Book", price = 50, iconRes = R.drawable.ic_play_book),
            StoreItem(id = 3, name = "Ball", price = 30, iconRes = R.drawable.ic_play_ball),
            StoreItem(id = 4, name = "Water", price = 5, iconRes = R.drawable.ic_food_water),
            StoreItem(id = 5, name = "Cake", price = 100, iconRes = R.drawable.ic_food_cake)
            // Add more items here as needed
        )
    }

    /*
     * Retrieves the list of purchased items from the database.
     */
    fun getPurchasedItems(): Flow<List<StoreItem>> {
        return storeItemDao.getAllItems()
    }

    /*
     * Inserts or updates a purchased item into the database.
     */
    suspend fun purchaseItem(item: StoreItem) {
        storeItemDao.upsert(item)
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
