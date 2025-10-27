/**
 * Top bar for the app.
 * Currently shows the app name and the user's coin count.
 * In the future, you can add icons (e.g. settings) here.
 */
package com.cse.tamagotchi.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitGotchiTopBar(coins: Int) {
    TopAppBar(
        title = { Text("HabitGotchi") },
        actions = {
            Text("Coins: $coins", style = MaterialTheme.typography.bodyLarge)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}