package com.cse.tamagotchi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cse.tamagotchi.R
import com.cse.tamagotchi.model.TamagotchiExpression
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class PetFaceState {
    Idle,    // opened eyes
    Blinking // closed eyes (or blinking, duh)
}

@Composable
fun AnimatedPet(
    expression: TamagotchiExpression,
    modifier: Modifier = Modifier,
    contentDescription: String
) {
    // hold current state of face
    var faceState by remember { mutableStateOf(PetFaceState.Idle) }

    // run blink animation loop
    LaunchedEffect(Unit) {
        while (true) {
            // wait for random time between 2 - 4 secs
            delay(Random.nextLong(2000, 4000))

            // switch to blinking state
            faceState = PetFaceState.Blinking

            // blink animation should play briefly.
            delay(170)

            // Return to idle state
            faceState = PetFaceState.Idle
        }
    }

    // 3. Choose the correct image resource based on BOTH expression and blink state
    val imageRes = when (expression) {
        TamagotchiExpression.HAPPY -> {
            // switches from default to blinking. happens when dog is happy (duh)
            if (faceState == PetFaceState.Idle) {
                R.drawable.ic_tamagotchi_happy
            } else {
                R.drawable.ic_tamagotchi_happy_closed
            }
        }
        TamagotchiExpression.SAD -> {
            // blink states
            if (faceState == PetFaceState.Idle) {
                R.drawable.ic_tamagotchi_sad
            } else {
                R.drawable.ic_tamagotchi_sad_closed
            }
        }
        TamagotchiExpression.NEUTRAL -> {
            // same as above
            if (faceState == PetFaceState.Idle) {
                R.drawable.ic_tamagotchi_neutral
            } else {
                R.drawable.ic_tamagotchi_neutral_closed
            }
        }
    }

    // renders image
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "My Tamagotchi",
        modifier = modifier
    )
}
