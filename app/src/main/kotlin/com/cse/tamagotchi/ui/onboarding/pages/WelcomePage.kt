package com.cse.tamagotchi.ui.onboarding.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.onboarding.components.PetBounce

@Composable
fun WelcomePage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ) {
        PetBounce("✨")
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Welcome to HabitGotchi!",
            textAlign = TextAlign.Center,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Build habits, stay motivated, and take care of your adorable digital pet!",
            textAlign = TextAlign.Center
        )
    }
}
