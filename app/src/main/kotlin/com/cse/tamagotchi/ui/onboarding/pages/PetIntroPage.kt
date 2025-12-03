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
fun PetIntroPage() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PetBounce("🐧")
        Spacer(Modifier.height(20.dp))

        Text("Meet your HabitGotchi!", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        Text(
            "Your pet grows happier as you complete tasks and stick to your habits.",
            textAlign = TextAlign.Center
        )
    }
}
