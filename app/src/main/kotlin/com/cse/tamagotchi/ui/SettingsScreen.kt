package com.cse.tamagotchi.ui

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.DestructiveRed
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isDarkMode by viewModel.isDarkMode.collectAsState(initial = false)
    var showDialog by remember { mutableStateOf(false) }
    val view = LocalView.current

    val greenButtonColors = ButtonDefaults.buttonColors(
        containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
        contentColor = if (isDarkMode) PureWhite else DarkGrey
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reset All Data", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Are you sure you want to reset all data? This action cannot be undone.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        viewModel.resetAppData()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DestructiveRed,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        showDialog = false
                    },
                    colors = greenButtonColors
                ) {
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
        Text("Settings", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark Mode", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
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
            colors = greenButtonColors,
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
            colors = ButtonDefaults.buttonColors(
                containerColor = DestructiveRed,
                contentColor = if (isDarkMode) PureWhite else DarkGrey
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset All Data")
        }
    }
}
