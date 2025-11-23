package com.bergenproduction.reminders.api

interface RemindersManager {

    fun setReminder(id: Int, time: Long)

    fun deleteReminder(id: Int)

    companion object {
        const val NOTIFICATION_ID = 121
        const val CHANNEL_ID = "channel1"
        const val TITLE_EXTRA = "titleExtra"
        const val MESSAGE_EXTRA = "messageExtra"
    }
}