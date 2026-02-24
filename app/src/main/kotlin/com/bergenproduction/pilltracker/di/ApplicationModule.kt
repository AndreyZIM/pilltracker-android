package com.bergenproduction.pilltracker.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.bergenproduction.pilltracker.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {

    companion object {

        private const val DATABASE_NAME = "pilltracker-database"

        @Provides
        @Singleton
        fun provideRoomDataBase(@ApplicationContext context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME).build()
    }
}