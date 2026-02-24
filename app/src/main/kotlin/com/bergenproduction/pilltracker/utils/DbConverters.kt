package com.bergenproduction.pilltracker.utils

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DbConverters {
    @TypeConverter
    fun fromIntArray(value: IntArray): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntArray(value: String): IntArray {
        return Json.decodeFromString(value)
    }
}