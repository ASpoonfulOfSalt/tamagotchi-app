package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.store.StoreHeader
import com.cse.tamagotchi.ui.store.StoreItemList
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
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                currentHat = uiState.currentHat,
                onPurchaseClick = viewModel::purchaseItem,
                onEquipClick = viewModel::equipHat,
                checkIfHat = { item -> viewModel.getHatFromItem(item) != null },
                getHatFromItem = viewModel::getHatFromItem,
                isDarkMode = isDarkMode
            )
        }
    }
}
