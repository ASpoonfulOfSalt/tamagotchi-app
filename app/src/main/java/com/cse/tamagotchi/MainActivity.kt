package com.cse.tamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cse.tamagotchi.ui.navigation.AppNavRoot
import com.cse.tamagotchi.ui.theme.TamagotchiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TamagotchiTheme {
                AppNavRoot()
            }
        }
    }
}