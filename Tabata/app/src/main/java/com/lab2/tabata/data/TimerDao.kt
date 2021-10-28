package com.lab2.tabata.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimerDao {
    @Query("SELECT * FROM Timers")
    suspend fun getTimers() : List<Timer>

    @Query("SELECT * FROM Timers WHERE id = :id")
    suspend fun getTimer(id : Int) : Timer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: Timer)

    @Update
    suspend fun updateTimer(timer: Timer)

    @Delete
    suspend fun deleteTimer(timer: Timer)

    @Query("DELETE FROM Timers")
    suspend fun clearTimers()
}