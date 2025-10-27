package com.cse.tamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.navigation.AppNavRoot
import com.cse.tamagotchi.ui.theme.TamagotchiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val userPrefs = UserPreferencesRepository(applicationContext)
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by userPrefs.isDarkMode.collectAsState(initial = false)
            TamagotchiTheme(darkTheme = isDarkMode) {
                AppNavRoot()
            }
        }
    }
}
