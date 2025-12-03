package com.cse.tamagotchi.repository

import com.cse.tamagotchi.R
import com.cse.tamagotchi.data.StoreItemDao
import com.cse.tamagotchi.model.StoreItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreRepository(private val storeItemDao: StoreItemDao) {
    
    private val availableItems: List<StoreItem> = listOf(
        // Consumables
        StoreItem(id = 1, name = "Apple", price = 10, iconRes = R.drawable.ic_food_apple, description = "A tasty snack!"),
        StoreItem(id = 2, name = "Book", price = 50, iconRes = R.drawable.ic_play_book, description = "Full of knowledge! (Read to improve your pet's experience!)"),
        StoreItem(id = 3, name = "Ball", price = 30, iconRes = R.drawable.ic_play_ball, description = "Fun for the whole family!"),
        StoreItem(id = 4, name = "Water", price = 5, iconRes = R.drawable.ic_food_water, description = "Everyone needs good ol' fashioned H2O."),
        StoreItem(id = 5, name = "Cake", price = 100, iconRes = R.drawable.ic_food_cake, description = "Part of a balanced breakfast!"),
        
        // Hats
        StoreItem(id = 10, name = "Baseball Cap", price = 150, iconRes = R.drawable.ic_hat_baseball, description = "A sporty hat!"),
        StoreItem(id = 11, name = "Beanie", price = 200, iconRes = R.drawable.ic_hat_beanie, description = "A warmy hat!"),
        StoreItem(id = 12, name = "Bucket Hat", price = 175, iconRes = R.drawable.ic_hat_bucket, description = "A stormy hat!"),
        StoreItem(id = 13, name = "Cowboy Hat", price = 250, iconRes = R.drawable.ic_hat_cowboy, description = "Gun sold separately..."),
        StoreItem(id = 14, name = "Party Hat", price = 120, iconRes = R.drawable.ic_hat_party, description = "A party hat!"),
        StoreItem(id = 15, name = "Top Hat", price = 300, iconRes = R.drawable.ic_hat_top, description = "A spiffy hat!")
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
