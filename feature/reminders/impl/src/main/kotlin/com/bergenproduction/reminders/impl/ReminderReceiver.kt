package com.bergenproduction.reminders.impl

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.bergenproduction.reminders.api.RemindersManager.Companion.CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


// TODO replace strings with resources
@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: PreparationRepository
    private val scope = CoroutineScope(Job())

    // Method called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        scope.launch(Dispatchers.IO) {
            val preparation = repository.getPreparation(intent.getIntExtra("reminder_id", -1))
            // Build the notification using NotificationCompat.Builder
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.round_medical_services_24)
                .setContentTitle("PillTracker") // Set title from intent
                .setContentText("Срок годности вашего препарата “${preparation.name}” истек. Пора его утилизировать!") // Set content text from intent
                .build()

            // Get the NotificationManager service
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Show the notification using the manager
            manager.notify(preparation.id, notification)

            pendingResult.finish()
        }
    }
}