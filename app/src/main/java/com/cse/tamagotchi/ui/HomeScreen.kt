package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.StoreViewModel

@Composable
fun HomeScreen(viewModel: StoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("HabitGotchi 🐾", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("Coins: ${uiState.userCoins}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { 
            view.playSoundEffect(SoundEffectConstants.CLICK)
            viewModel.addCoins(10) 
        }) {
            Text("Feed Tamagotchi")
        }
    }
}
