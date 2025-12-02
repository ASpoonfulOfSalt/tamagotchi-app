package com.cse.tamagotchi.ui.onboarding

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.onboarding.components.*
import com.cse.tamagotchi.ui.onboarding.pages.*

@Composable
fun OnboardingScreen(
    onComplete: (String, Boolean) -> Unit
) {
    var page by remember { mutableStateOf(0) }
    var petName by remember { mutableStateOf("") }
    var prefersDark by remember { mutableStateOf(false) }

    val pages = listOf<@Composable () -> Unit>(
        { WelcomePage() },
        { PetIntroPage() },
        { HowItWorksPage() },
        { NamePage(petName) { petName = it } },
        { ThemePage(prefersDark) { prefersDark = it } },
        { FinishPage { onComplete(petName, prefersDark) } }
    )

    OnboardingBackground(prefersDark = prefersDark) {
        Box(Modifier.fillMaxSize()) {
            pages[page]()

            if (page < pages.size - 1) {
                OnboardingNavButtons(
                    showBack = page > 0,
                    onBack = { page-- },
                    onNext = {
                        // Require pet name on the name page
                        if (page == 3 && petName.isBlank()) return@OnboardingNavButtons
                        page++
                    }
                )
            }
        }
    }
}
