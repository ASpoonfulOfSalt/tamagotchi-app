package com.cse.tamagotchi.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.cse.tamagotchi.MainActivity
import com.cse.tamagotchi.R

class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        // Create an intent to launch the app when the notification is tapped
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1, // A unique request code
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_tamagotchi_neutral) // A good icon for notifications
            .setContentTitle("Your pet needs you!")
            .setContentText("Don't forget to check on ${petName ?: "your Tamagotchi"}.") // Use a dynamic name if possible
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(activityPendingIntent) // Set the tap action
            .setAutoCancel(true) // Removes the notification when tapped
            .build()

        // Show it
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "tamagotchi_channel"
        const val NOTIFICATION_ID = 1
        var petName: String? = "your Tamagotchi" // You can update this from your ViewModel
    }
}
