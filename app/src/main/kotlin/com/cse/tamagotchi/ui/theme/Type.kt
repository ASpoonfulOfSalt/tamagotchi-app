package com.cse.tamagotchi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cse.tamagotchi.R

val Chewy = FontFamily(
    Font(R.font.chewy_regular, FontWeight.Normal)
)

val Typography = Typography(
    // keep chewy font as the default for headers and body text
    bodyLarge = TextStyle(
        fontFamily = Chewy,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Chewy,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Chewy,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp
    ),
    // keep plain default bold for all buttons
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )
)
