package com.cse.tamagotchi.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.ui.components.AnimatedPet
import com.cse.tamagotchi.viewmodel.TamagotchiUiState
import androidx.compose.ui.res.painterResource

@Composable
fun PetDisplay(uiState: TamagotchiUiState) {
    Box(contentAlignment = Alignment.TopCenter) {

        Crossfade(uiState.tamagotchi.expression, label = "pet") {
            AnimatedPet(expression = it, modifier = Modifier.size(160.dp), contentDescription = "My Tamagotchi")
        }

        uiState.tamagotchi.currentHat?.let { hat ->
            Image(
                painter = painterResource(id = hat.drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .offset(y = hat.yOffset.dp, x = hat.xOffset.dp)
            )
        }

        uiState.speechBubbleMessage?.let {
            SpeechBubble(
                message = it,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = 60.dp, y = (-20).dp)
            )
        }
    }
}
