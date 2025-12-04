package com.cse.tamagotchi.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cse.tamagotchi.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

@Composable
fun SpeechBubble(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(120.dp, 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_thought_bubble),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = message,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp, start = 35.dp)
        )
    }
}
