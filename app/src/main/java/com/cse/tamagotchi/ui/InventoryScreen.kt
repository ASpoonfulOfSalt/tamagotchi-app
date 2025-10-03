package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.StoreViewModel

@Composable
fun InventoryScreen(
    viewModel: StoreViewModel,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()
    val inventory = uiState.userInventory

    if (inventory.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Your inventory is empty.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(inventory) { item ->
                ListItem(
                    headlineContent = { Text("${item.name} (x${item.quantity})") },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = item.name,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                )
            }
        }
    }
}
