package com.cse.tamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cse.tamagotchi.audio.BackgroundMusicManager
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.navigation.AppNavRoot
import com.cse.tamagotchi.ui.theme.TamagotchiTheme

class MainActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPreferencesRepository(applicationContext)

        // Start background music
        BackgroundMusicManager.start(this)

        setContent {
            val isDarkMode by userPrefs.isDarkMode.collectAsState(initial = false)
            TamagotchiTheme(darkTheme = isDarkMode) {
                AppNavRoot()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        BackgroundMusicManager.pause()
    }

    override fun onResume() {
        super.onResume()
        BackgroundMusicManager.start(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BackgroundMusicManager.stop()
    }
}
