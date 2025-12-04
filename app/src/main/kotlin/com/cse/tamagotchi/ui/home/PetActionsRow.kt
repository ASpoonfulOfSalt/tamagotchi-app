package com.cse.tamagotchi.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.viewmodel.TamagotchiUiState
import com.cse.tamagotchi.ui.theme.*

@Composable
fun PetActionsRow(
    uiState: TamagotchiUiState,
    showFoodMenu: Boolean,
    onShowFoodMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onFeed: (String) -> Unit,
    onWater: () -> Unit,
    onPlay: () -> Unit,
    isDarkMode: Boolean
) {
    val tamagotchi = uiState.tamagotchi
    val hunger = tamagotchi.hunger
    val water = tamagotchi.water

    val inv = uiState.inventory
    val hasApple = inv.any { it.name == "Apple" && it.quantity > 0 }
    val hasCake = inv.any { it.name == "Cake" && it.quantity > 0 }

    val colors = ButtonDefaults.buttonColors(
        containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
        contentColor = if (isDarkMode) PureWhite else DarkGrey
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        // -------------------------
        // FEED BUTTON
        // -------------------------
        Box {
            Button(
                onClick = {
                    // 🔒 Prevent feeding when hunger basically maxed
                    if (hunger >= 98) return@Button

                    if (hasApple && hasCake) {
                        onShowFoodMenu()
                    } else if (hasCake) {
                        onFeed("Cake")
                    } else {
                        onFeed("Apple")
                    }
                },
                colors = colors
            ) {
                Text("Feed")
            }

            DropdownMenu(
                expanded = showFoodMenu,
                onDismissRequest = onDismissMenu
            ) {
                DropdownMenuItem(
                    text = { Text("Apple") },
                    onClick = {
                        onDismissMenu()
                        if (hunger < 98) onFeed("Apple")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cake") },
                    onClick = {
                        onDismissMenu()
                        if (hunger < 98) onFeed("Cake")
                    }
                )
            }
        }

        // -------------------------
        // WATER BUTTON
        // -------------------------
        Button(
            onClick = {
                // 🔒 Block watering when water level is already high
                if (water >= 98) return@Button
                onWater()
            },
            colors = colors
        ) {
            Text("Water")
        }

        // -------------------------
        // PLAY BUTTON (always allowed)
        // -------------------------
        Button(onClick = onPlay, colors = colors) {
            Text("Play")
        }
    }
}
