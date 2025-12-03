package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.Hat
import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.ui.theme.TextGrey
import com.cse.tamagotchi.viewmodel.StoreViewModel

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel,
    isDarkMode: Boolean
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.purchaseMessage) {
        uiState.purchaseMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onPurchaseMessageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent, // Make scaffold transparent
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            StoreHeader(coins = uiState.userCoins)
            Spacer(modifier = Modifier.height(16.dp))
            StoreItemList(
                items = uiState.items,
                inventory = uiState.userInventory,
                currentHat = uiState.currentHat, // checks current hat type (if any)
                onPurchaseClick = { item ->
                    viewModel.purchaseItem(item)
                },
                onEquipClick = { item ->
                    viewModel.equipHat(item)
                },
                checkIfHat = { item ->
                    viewModel.getHatFromItem(item) != null
                },
                getHatFromItem = { item ->
                    viewModel.getHatFromItem(item)
                },
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun StoreHeader(coins: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Shop", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Text(text = "Coins: $coins", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun StoreItemList(
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            // Find quantity from userInventory
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

@Composable
private fun StoreItemRow(
    item: StoreItem,
    quantityOwned: Int,
    currentHat: Hat?,
    onPurchaseClick: (StoreItem) -> Unit,
    onEquipClick: (StoreItem) -> Unit,
    isHat: Boolean,
    hatType: Hat?,
    isDarkMode: Boolean
) {
    val view = LocalView.current
    val isEquipped = isHat && (currentHat == hatType)
    val canEquip = isHat && quantityOwned > 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.name,
                modifier = Modifier.size(48.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(text = item.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                
                if (quantityOwned > 0) {
                    Text(text = "Owned: $quantityOwned", color = TextGrey)
                } else {
                    Text(text = "Price: ${item.price} coins", color = MaterialTheme.colorScheme.onBackground)
                }
            }
            
            Button(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    if (canEquip) {
                        onEquipClick(item)
                    } else {
                        onPurchaseClick(item)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEquipped) TextGrey else if (isDarkMode) DarkModeGreen else LightModeGreen,
                    contentColor = if (isDarkMode) PureWhite else DarkGrey
                )
            ) {
                Text(
                    text = when {
                        isEquipped -> "Unequip"
                        canEquip -> "Equip"
                        else -> "Buy"
                    }
                )
            }
        }
    }
}
