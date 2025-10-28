package com.cse.tamagotchi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cse.tamagotchi.audio.BackgroundMusicManager
import androidx.core.content.ContextCompat
import com.cse.tamagotchi.notifications.NotificationScheduler
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.navigation.AppNavRoot
import com.cse.tamagotchi.ui.theme.TamagotchiTheme

class MainActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferencesRepository
    // --- ADD THIS BLOCK: Handles the result of the permission request ---
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Schedule the notifications.
            val scheduler = NotificationScheduler(this)
            scheduler.scheduleRepeatingNotification()
        } else {
            // Optional: Explain to the user that the feature is unavailable
            // because the permission was denied. You could show a Snackbar or a dialog.
        }
    }

    // --- ADD THIS FUNCTION: Checks for permission and either requests it or schedules notifications ---
    private fun askNotificationPermission() {
        // This is only required for Android 13 (API 33) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted, so schedule the notifications.
                    val scheduler = NotificationScheduler(this)
                    scheduler.scheduleRepeatingNotification()
                }
                // Optional: You can add a check for shouldShowRequestPermissionRationale here
                // to show a custom explanation dialog before requesting the permission.
                else -> {
                    // Directly ask for the permission.
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // For Android 12 and below, the permission is granted by default from the manifest.
            val scheduler = NotificationScheduler(this)
            scheduler.scheduleRepeatingNotification()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPreferencesRepository(applicationContext)

        // Start background music
        BackgroundMusicManager.start(this)

        // --- ADD THIS LINE: Call the function to handle notification permissions ---
        askNotificationPermission()

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
