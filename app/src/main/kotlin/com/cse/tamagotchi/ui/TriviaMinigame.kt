package com.cse.tamagotchi.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cse.tamagotchi.model.TriviaQuestion
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

data class IncorrectAnswer(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String
)

@Composable
fun TriviaMinigameDialog(
    onDismiss: () -> Unit,
    onGameFinished: (correctCount: Int) -> Unit
) {
    val context = LocalContext.current
    var questions by remember { mutableStateOf<List<TriviaQuestion>>(emptyList()) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isGameOver by remember { mutableStateOf(false) }
    var showExitConfirmation by remember { mutableStateOf(false) }
    var incorrectAnswers by remember { mutableStateOf<List<IncorrectAnswer>>(emptyList()) }

    LaunchedEffect(Unit) {
        questions = loadTriviaQuestions(context).shuffled().take(5)
    }

    if (questions.isEmpty()) {
        // Loading state or error handling could go here, but for now we just return
        return
    }
    
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Exit Quiz?") },
            text = { Text("Are you sure you want to exit the quiz? All progress will be lost and a book will be consumed regardless.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmation = false
                        onDismiss()
                    }
                ) {
                    Text("Exit")
                }
            },
            dismissButton = {
                Button(onClick = { showExitConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Dialog(
        onDismissRequest = {
             if (!isGameOver) {
                 showExitConfirmation = true
             }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isGameOver) {
                    Text(
                        text = "Game Over!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You got $score out of ${questions.size} correct!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (incorrectAnswers.isNotEmpty()) {
                        Text(
                            text = "Review Incorrect Answers:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(incorrectAnswers) { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = item.question,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Your Answer: ${item.userAnswer}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = "Correct Answer: ${item.correctAnswer}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Perfect Score! Well done!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onGameFinished(score) }) {
                        Text("Collect XP")
                    }
                } else {
                    val question = questions[currentQuestionIndex]
                    
                    Text(
                        text = "Question ${currentQuestionIndex + 1} / ${questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    // Progress Indicator
                    LinearProgressIndicator(
                        progress = { (currentQuestionIndex + 1) / questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    question.options.forEachIndexed { index, option ->
                        val isSelected = selectedOptionIndex == index
                        val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .selectable(
                                    selected = isSelected,
                                    onClick = { selectedOptionIndex = index }
                                ),
                            colors = CardDefaults.cardColors(containerColor = containerColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = {
                            val currentIndex = selectedOptionIndex
                            if (currentIndex != null) {
                                if (currentIndex == question.correctIndex) {
                                    score++
                                } else {
                                    val wrongAnswer = IncorrectAnswer(
                                        question = question.question,
                                        userAnswer = question.options[currentIndex],
                                        correctAnswer = question.options[question.correctIndex]
                                    )
                                    incorrectAnswers = incorrectAnswers + wrongAnswer
                                }
                                
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedOptionIndex = null
                                } else {
                                    isGameOver = true
                                }
                            }
                        },
                        enabled = selectedOptionIndex != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (currentQuestionIndex < questions.size - 1) "Next" else "Finish")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showExitConfirmation = true }) {
                         Text("Cancel")
                    }
                }
            }
        }
    }
}

fun loadTriviaQuestions(context: Context): List<TriviaQuestion> {
    return try {
        val inputStream = context.assets.open("trivia.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = reader.readText()
        Json.decodeFromString<List<TriviaQuestion>>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
