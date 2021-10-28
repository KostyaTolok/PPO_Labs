package com.lab2.tabata.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Timers")
data class Timer(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "warm_up_time") var warmUpTime: Int,
    @ColumnInfo(name = "work_time") var workTime: Int,
    @ColumnInfo(name = "rest_time") var restTime: Int,
    @ColumnInfo(name = "repeats") var repeats: Int,
    @ColumnInfo(name = "cycles") var cycles: Int,
    @ColumnInfo(name = "cooldownTime") var cooldownTime: Int,
    @ColumnInfo(name = "color") var color: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}