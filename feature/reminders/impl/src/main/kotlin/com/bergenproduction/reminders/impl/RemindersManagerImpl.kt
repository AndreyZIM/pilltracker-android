package com.bergenproduction.reminders.impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bergenproduction.reminders.api.RemindersManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RemindersManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RemindersManager {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun setReminder(id: Int, time: Long) {

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("reminder_id", id)
        }

        val formater = SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.getDefault())


        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        println("Scheduled ($id) : ${formater.format(Date(time))}")
    }

    override fun deleteReminder(id: Int) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("reminder_id", id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}