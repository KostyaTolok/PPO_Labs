package com.lab2.tabata.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Timer::class], version = 1)
abstract class TimersDatabase: RoomDatabase() {
    abstract fun timerDao(): TimerDao

    companion object {
        var timersDatabase: TimersDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): TimersDatabase {
            if (timersDatabase == null) {
                timersDatabase = Room.databaseBuilder(
                    context, TimersDatabase::class.java,
                    "timers.db"
                ).build()
            }
            return timersDatabase!!
        }
    }
}