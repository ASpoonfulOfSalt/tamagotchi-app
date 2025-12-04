package com.cse.tamagotchi.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cse.tamagotchi.R
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.home.*
import com.cse.tamagotchi.viewmodel.TamagotchiViewModel
import com.cse.tamagotchi.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    tamagotchiViewModel: TamagotchiViewModel,
    isDarkMode: Boolean,
    userPrefs: UserPreferencesRepository,
    taskViewModel: TaskViewModel
) {
    val uiState by tamagotchiViewModel.uiState.collectAsStateWithLifecycle()
    val taskUiState by taskViewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val xp by userPrefs.userXp.collectAsState(initial = 0)
    val level by userPrefs.userLevel.collectAsState(initial = 1)

    val snackbar = remember { SnackbarHostState() }

    var showRename by rememberSaveable { mutableStateOf(false) }
    var showFoodMenu by remember { mutableStateOf(false) }
    var showXpMenu by remember { mutableStateOf(false) }
    var showTrivia by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    val isNight by produceIsNightState()

    // Listen for streak XP reward
    LaunchedEffect(taskUiState.levelUpReward) {
        if (taskUiState.levelUpReward > 0) showConfetti = true
    }

    // Pet message (snackbar)
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbar.showSnackbar(it)
            tamagotchiViewModel.userMessageShown()
        }
    }

    // Trivia result handler
    if (showTrivia) {
        TriviaMenuDialog(
            onDismiss = { showTrivia = false },
            onFinish = { correct ->
                showTrivia = false
                var xpEarn = correct * 10
                if (correct == 5) xpEarn += 50
                scope.launch { userPrefs.addXp(xpEarn) }
            }
        )
    }

    // Rename dialog
    if (showRename) {
        RenamePetDialog(
            onDismiss = { showRename = false },
            onConfirm = { name ->
                tamagotchiViewModel.renamePet(name)
                showRename = false
            }
        )
    }

    // Level up rewards
    if (taskUiState.levelUpReward > 0) {
        LevelUpDialog(
            reward = taskUiState.levelUpReward,
            onDismiss = { taskViewModel.onLevelUpRewardShown() }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {

            // Background icons
            Image(
                painter = painterResource(id = if (isNight) R.drawable.ic_night_moon else R.drawable.ic_day_sun),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd).padding(24.dp).size(115.dp)
            )
            Image(
                painter = painterResource(id = if (isNight) R.drawable.ic_night_stars else R.drawable.ic_day_clouds),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopStart).padding(24.dp).size(110.dp)
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                Column(
                    Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // PET NAME
                    Text(
                        text = uiState.tamagotchi.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { showRename = true }
                    )

                    Spacer(Modifier.height(8.dp))

                    // PET SPRITE AREA
                    PetDisplay(uiState = uiState)

                    // XP BAR + Trivia dropdown
                    XpBarSection(
                        level = level,
                        xp = xp,
                        showXpMenu = showXpMenu,
                        onXpMenuClick = { showXpMenu = true },
                        onDismissXpMenu = { showXpMenu = false },
                        onPlayTrivia = {
                            tamagotchiViewModel.readBook {
                                showTrivia = true
                            }
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    // HUNGER / WATER / HAPPINESS
                    PetStatsSection(uiState.tamagotchi)

                    Spacer(Modifier.height(16.dp))

                    // FEED / WATER / PLAY BUTTONS
                    PetActionsRow(
                        uiState = uiState,
                        showFoodMenu = showFoodMenu,
                        onShowFoodMenu = { showFoodMenu = true },
                        onDismissMenu = { showFoodMenu = false },
                        onFeed = { food -> tamagotchiViewModel.feedPet(food) },
                        onWater = { tamagotchiViewModel.hydratePet() },
                        onPlay = { tamagotchiViewModel.playPet() },
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.height(24.dp))

                    // STREAK
                    StreakDisplay(uiState.tamagotchi)
                }

                if (showConfetti) {
                    LevelUpConfetti { showConfetti = false }
                }
            }
        }
    }
}
