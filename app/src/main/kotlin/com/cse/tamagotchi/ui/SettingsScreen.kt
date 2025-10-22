package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)
    var showDialog by remember { mutableStateOf(false) }
    val view = LocalView.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reset All Data") },
            text = { Text("Are you sure you want to reset all data? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        viewModel.resetAppData()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                Button(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

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
                onCheckedChange = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    viewModel.toggleDarkMode(it)
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.resetPetStats() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Pet Stats (Testing)")
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset All Data")
        }
    }
}
