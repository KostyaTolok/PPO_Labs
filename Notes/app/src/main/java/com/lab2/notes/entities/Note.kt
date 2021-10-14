package com.lab2.notes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
class Note(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date_time") var dateTime: String,
    @ColumnInfo(name = "text") var text: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    override fun toString(): String {
        return "$title: $dateTime"
    }
}
