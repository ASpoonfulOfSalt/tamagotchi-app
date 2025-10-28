package com.cse.tamagotchi.notifications


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun scheduleRepeatingNotification() {
        val interval = TimeUnit.HOURS.toMillis(3)
        val firstTriggerTime = System.currentTimeMillis() + interval

        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                firstTriggerTime,
                interval,
                getPendingIntent()
            )
        } catch (e: SecurityException) {
        }
    }

    fun cancelNotifications() {
        alarmManager.cancel(getPendingIntent())
    }
}
