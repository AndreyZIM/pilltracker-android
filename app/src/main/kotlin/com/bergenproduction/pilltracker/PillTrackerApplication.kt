package com.bergenproduction.pilltracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.bergenproduction.reminders.api.RemindersManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PillTrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(RemindersManager.CHANNEL_ID, CHANNEL_NAME, importance)
        channel.description = CHANNEL_DESC

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_NAME = "Notify Channel"
        const val CHANNEL_DESC = "A Description of the Channel"
    }
}