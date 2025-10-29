package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(
    onComplete: (String, Boolean) -> Unit
) {
    var step by remember { mutableStateOf(0) }
    var petName by remember { mutableStateOf("") }
    var prefersDark by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            when (step) {
                0 -> {
                    Text(
                        text = "Welcome to HabitGotchi!",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Grow good habits and take care of your Tamagotchi pet as you stay productive!",
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { step++ }) { Text("Next") }
                }

                1 -> {
                    Text("Name your Tamagotchi:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = petName,
                        onValueChange = { petName = it },
                        label = { Text("Pet name") }
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { if (petName.isNotBlank()) step++ }) {
                        Text("Continue")
                    }
                }

                2 -> {
                    Text("Choose your theme", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { prefersDark = false; step++ }) { Text("Light") }
                        Button(onClick = { prefersDark = true; step++ }) { Text("Dark") }
                    }
                }

                3 -> {
                    Text("You're all set!", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(12.dp))
                    Text("Tap below to start caring for your pet!")
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { onComplete(petName, prefersDark) }) { Text("Start Playing") }
                }
            }
        }
    }
}
