package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark Mode", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = { viewModel.resetAppData() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset All Data")
        }
    }
}
