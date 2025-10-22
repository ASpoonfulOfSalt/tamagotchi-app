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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.model.StoreItem
import com.cse.tamagotchi.viewmodel.StoreViewModel

@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel
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
                onPurchaseClick = { item ->
                    viewModel.purchaseItem(item)
                }
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
        Text(text = "Shop", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Coins: $coins", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun StoreItemList(items: List<StoreItem>, onPurchaseClick: (StoreItem) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            StoreItemRow(item = item, onPurchaseClick = onPurchaseClick)
        }
    }
}

@Composable
private fun StoreItemRow(item: StoreItem, onPurchaseClick: (StoreItem) -> Unit) {
    val view = LocalView.current
    Card(
        modifier = Modifier.fillMaxWidth()
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
                Text(text = item.name, fontWeight = FontWeight.Bold)
                Text(text = "Price: ${item.price} coins")
            }
            Button(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onPurchaseClick(item)
            }) {
                Text("Buy")
            }
        }
    }
}
