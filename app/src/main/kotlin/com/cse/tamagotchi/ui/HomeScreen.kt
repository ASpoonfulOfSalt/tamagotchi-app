package com.cse.tamagotchi.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cse.tamagotchi.R
import com.cse.tamagotchi.model.TamagotchiExpression
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.theme.DarkModeGreen
import com.cse.tamagotchi.ui.theme.DarkGrey
import com.cse.tamagotchi.ui.theme.LightModeGreen
import com.cse.tamagotchi.ui.theme.PureWhite
import com.cse.tamagotchi.viewmodel.TamagotchiViewModel
import com.cse.tamagotchi.viewmodel.TaskViewModel
import java.util.Calendar
import kotlin.random.Random


@Composable
fun produceIsNightState(): State<Boolean> {
    val isNight = remember { mutableStateOf(false) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val updateTime: () -> Unit = {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        isNight.value = currentHour >= 21 || currentHour < 9
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                updateTime()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        updateTime()

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return isNight
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tamagotchiViewModel: TamagotchiViewModel, 
    isDarkMode: Boolean, 
    userPreferencesRepository: UserPreferencesRepository, 
    taskViewModel: TaskViewModel
) {
    val tamagotchiUiState by tamagotchiViewModel.uiState.collectAsStateWithLifecycle()
    val taskUiState by taskViewModel.uiState.collectAsStateWithLifecycle()
    val tamagotchi = tamagotchiUiState.tamagotchi
    val snackbarHostState = remember { SnackbarHostState() }
    var showRenameDialog by rememberSaveable { mutableStateOf(false) }
    var newName by rememberSaveable { mutableStateOf("") }

    val userXp by userPreferencesRepository.userXp.collectAsState(initial = 0)
    val userLevel by userPreferencesRepository.userLevel.collectAsState(initial = 1)

    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(taskUiState.levelUpReward) {
        if (taskUiState.levelUpReward > 0) {
            showConfetti = true
        }
    }

    if (taskUiState.levelUpReward > 0) {
        LevelUpDialog(
            reward = taskUiState.levelUpReward,
            onDismiss = { taskViewModel.onLevelUpRewardShown() }
        )
    }

    val isNight by produceIsNightState()

    LaunchedEffect(tamagotchiUiState.userMessage) {
        tamagotchiUiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            tamagotchiViewModel.userMessageShown()
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
                            tamagotchiViewModel.renamePet(newName)
                            showRenameDialog = false
                            newName = ""
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = { Button(onClick = { showRenameDialog = false }) { Text("Cancel") } }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(snackbarData = data, containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
        } }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Image(painter = painterResource(id = if (isNight) R.drawable.ic_night_moon else R.drawable.ic_day_sun), contentDescription = null, modifier = Modifier.padding(24.dp).size(125.dp).align(Alignment.TopEnd))
            Image(painter = painterResource(id = if (isNight) R.drawable.ic_night_stars else R.drawable.ic_day_clouds), contentDescription = null, modifier = Modifier.padding(24.dp).size(110.dp).align(Alignment.TopStart))

            if (tamagotchiUiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp).offset(y = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    XpBar(xp = userXp, level = userLevel, modifier = Modifier.fillMaxWidth(0.8f))
                    Spacer(Modifier.height(16.dp))
                    Text(tamagotchi.name, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.clickable { showRenameDialog = true })
                    Spacer(Modifier.height(8.dp))
                    Crossfade(targetState = tamagotchi.expression, label = "pet-expression") {
                        expression ->
                        Image(painter = painterResource(id = when (expression) {
                            TamagotchiExpression.HAPPY -> R.drawable.ic_tamagotchi_happy
                            TamagotchiExpression.NEUTRAL -> R.drawable.ic_tamagotchi_neutral
                            TamagotchiExpression.SAD -> R.drawable.ic_tamagotchi_sad
                        }), contentDescription = "Tamagotchi Expression", modifier = Modifier.size(160.dp))
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Hunger: ${tamagotchi.hunger}", color = MaterialTheme.colorScheme.onBackground)
                    Text("Water: ${tamagotchi.water}", color = MaterialTheme.colorScheme.onBackground)
                    Text("Happiness: ${tamagotchi.happiness}", color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(16.dp))
                    val buttonColors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) DarkModeGreen else LightModeGreen, contentColor = if (isDarkMode) PureWhite else DarkGrey)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { tamagotchiViewModel.feedPet() }, colors = buttonColors) { Text("Feed") }
                        Button(onClick = { tamagotchiViewModel.hydratePet() }, colors = buttonColors) { Text("Water") }
                        Button(onClick = { tamagotchiViewModel.playPet() }, colors = buttonColors) { Text("Play") }
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("🔥 Daily Streak: ${tamagotchi.streakCount}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                }

                if (showConfetti) {
                    LevelUpConfetti(modifier = Modifier.fillMaxSize()) { showConfetti = false }
                }
            }
        }
    }
}

@Composable
fun LevelUpDialog(reward: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Congrats!") },
        text = { Text("You leveled up! You received $reward coins!") },
        confirmButton = { Button(onClick = onDismiss) { Text("Awesome!") } }
    )
}

@Composable
fun XpBar(xp: Int, level: Int, modifier: Modifier = Modifier) {
    val xpForNextLevel = 100 * level
    val progress = xp.toFloat() / xpForNextLevel.toFloat()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Level: $level", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
            Text("$xp / $xpForNextLevel XP", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
fun LevelUpConfetti(modifier: Modifier = Modifier, onAnimationFinished: () -> Unit) {
    var animStarted by remember { mutableStateOf(false) }
    val particleCount = 150
    val particles = remember { List(particleCount) { ConfettiParticle() } }

    val animationProgress by animateFloatAsState(
        targetValue = if (animStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "confetti",
        finishedListener = { onAnimationFinished() }
    )

    LaunchedEffect(Unit) { animStarted = true }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        for (particle in particles) {
            val (offset, alpha, rotation) = particle.update(animationProgress, canvasWidth, canvasHeight)
            if (alpha > 0) {
                rotate(degrees = rotation, pivot = offset) {
                    drawRect(
                        color = particle.color,
                        topLeft = offset,
                        size = particle.size,
                        alpha = alpha
                    )
                }
            }
        }
    }
}

data class ConfettiParticle(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotationSpeed: Float,
    val size: androidx.compose.ui.geometry.Size
) {
    constructor() : this(
        color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), alpha = 1f),
        startX = Random.nextFloat(),
        startY = Random.nextFloat() * -0.2f, // Start above the screen
        velocityX = Random.nextFloat() * 200 - 100, // Horizontal movement
        velocityY = Random.nextFloat() * 300 + 400, // Downward speed
        rotationSpeed = Random.nextFloat() * 720 - 360, // Spin
        size = androidx.compose.ui.geometry.Size(width = Random.nextFloat() * 20 + 15, height = Random.nextFloat() * 10 + 10)
    )

    fun update(progress: Float, canvasWidth: Float, canvasHeight: Float): Triple<Offset, Float, Float> {
        val time = progress * 2.5f
        val newX = startX * canvasWidth + velocityX * time
        val newY = startY * canvasHeight + velocityY * time

        val alpha = (1f - progress).coerceIn(0f, 1f)
        val rotation = rotationSpeed * time

        return Triple(Offset(newX, newY), alpha, rotation)
    }
}

