package com.cse.tamagotchi.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cse.tamagotchi.R
import com.cse.tamagotchi.model.TamagotchiExpression
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.viewmodel.TamagotchiViewModel
import java.util.Calendar

// --- MODIFIED FUNCTION ---
// This function now checks the time only once, preventing the recomposition loop.
@Composable
fun isNightMode(): Boolean {
    // 'remember' ensures this calculation runs only once when the composable is first created.
    return remember {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        // Night mode is from 9 PM (21) to 8:59 AM (8)
        currentHour >= 21 || currentHour < 9
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TamagotchiViewModel, isDarkMode: Boolean) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tamagotchi = uiState.tamagotchi
    val snackbarHostState = remember { SnackbarHostState() }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var newName by rememberSaveable { mutableStateOf("") }
    // Call the new, simplified function
    val isNight = isNightMode()

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.userMessageShown()
        }
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Pet") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.renamePet(newName)
                            showRenameDialog = false
                            newName = ""
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showRenameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Sun/Moon in the top-right corner
            Image(
                painter = painterResource(id = if (isNight) R.drawable.ic_night_moon else R.drawable.ic_day_sun),
                contentDescription = if (isNight) "Night icon" else "Day icon",
                modifier = Modifier
                    .padding(24.dp)
                    .size(125.dp)
                    .align(Alignment.TopEnd) // Always in the top right
            )

            // Stars/Clouds in the top-left corner
            Image(
                painter = painterResource(id = if (isNight) R.drawable.ic_night_stars else R.drawable.ic_day_clouds),
                contentDescription = if (isNight) "Stars icon" else "Clouds icon",
                modifier = Modifier
                    .padding(24.dp)
                    .size(110.dp)
                    .align(Alignment.TopStart) // Always in the top left
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .offset(y = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tamagotchi.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { showRenameDialog = true }
                    )

                    Spacer(Modifier.height(8.dp))

                    Crossfade(targetState = tamagotchi.expression, label = "pet-expression") {
                            expression ->
                        Image(
                            painter = painterResource(
                                id = when (expression) {
                                    TamagotchiExpression.HAPPY -> R.drawable.ic_tamagotchi_happy
                                    TamagotchiExpression.NEUTRAL -> R.drawable.ic_tamagotchi_neutral
                                    TamagotchiExpression.SAD -> R.drawable.ic_tamagotchi_sad
                                }
                            ),
                            contentDescription = "Tamagotchi Expression",
                            modifier = Modifier.size(160.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text("Hunger: ${tamagotchi.hunger}", color = MaterialTheme.colorScheme.onBackground)
                    Text("Water: ${tamagotchi.water}", color = MaterialTheme.colorScheme.onBackground)
                    Text("Happiness: ${tamagotchi.happiness}", color = MaterialTheme.colorScheme.onBackground)

                    Spacer(Modifier.height(16.dp))

                    val buttonColors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen,
                        contentColor = if (isDarkMode) PureWhite else DarkGrey
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.feedPet() }, colors = buttonColors) { Text("Feed") }
                        Button(onClick = { viewModel.hydratePet() }, colors = buttonColors) { Text("Water") }
                        Button(onClick = { viewModel.playPet() }, colors = buttonColors) { Text("Play") }
                    }

                    // --- ADD THIS BLOCK BACK ---
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "🔥 Daily Streak: ${tamagotchi.streakCount}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    // --- END OF NEW BLOCK ---
                }
            }
        }
    }
}
