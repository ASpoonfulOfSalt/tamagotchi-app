package com.cse.tamagotchi.ui.onboarding.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HowItWorksPage() {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "How it works",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Text("✓ Complete daily & weekly tasks", textAlign = TextAlign.Center)
        Text("✓ Earn coins & XP", textAlign = TextAlign.Center)
        Text("✓ Buy items to care for your pet", textAlign = TextAlign.Center)
        Text("✓ Keep your HabitGotchi happy!", textAlign = TextAlign.Center)
    }
}
