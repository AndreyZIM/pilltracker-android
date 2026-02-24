package com.bergenproduction.pilltracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bergenproduction.pilltracker.utils.DbConverters

@Database(
    entities = [

    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DbConverters::class)
abstract class AppDataBase: RoomDatabase() {
}