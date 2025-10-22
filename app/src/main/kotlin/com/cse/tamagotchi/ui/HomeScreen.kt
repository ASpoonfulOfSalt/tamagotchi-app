package com.cse.tamagotchi.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cse.tamagotchi.viewmodel.TamagotchiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TamagotchiViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tamagotchi = uiState.tamagotchi
    val snackbarHostState = remember { SnackbarHostState() }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var newName by rememberSaveable { mutableStateOf("") }

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
            // background sky
            Box(modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF87CEEB)) // replace with Image for your SVG
            )

            // grass at bottom
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF2E8B57))
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tamagotchi.name, 
                        style = MaterialTheme.typography.headlineMedium,
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

                    Text("Hunger: ${tamagotchi.hunger}")
                    Text("Water: ${tamagotchi.water}")
                    Text("Happiness: ${tamagotchi.happiness}")

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.feedPet() }) { Text("Feed") }
                        Button(onClick = { viewModel.hydratePet() }) { Text("Water") }
                        Button(onClick = { viewModel.playPet() }) { Text("Play") }
                    }
                }
            }
        }
    }
}
