package com.cse.tamagotchi.ui.store

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Hat
import com.cse.tamagotchi.model.StoreItem

@Composable
fun StoreItemList(
    items: List<StoreItem>,
    inventory: List<StoreItem>,
    currentHat: Hat?,
    onPurchaseClick: (StoreItem) -> Unit,
    onEquipClick: (StoreItem) -> Unit,
    checkIfHat: (StoreItem) -> Boolean,
    getHatFromItem: (StoreItem) -> Hat?,
    isDarkMode: Boolean
) {
    LazyColumn(
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
    ) {
        items(items) { item ->
            val ownedQuantity = inventory.find { it.id == item.id }?.quantity ?: 0

            StoreItemRow(
                item = item,
                quantityOwned = ownedQuantity,
                currentHat = currentHat,
                onPurchaseClick = onPurchaseClick,
                onEquipClick = onEquipClick,
                isHat = checkIfHat(item),
                hatType = getHatFromItem(item),
                isDarkMode = isDarkMode
            )
        }
    }
}
