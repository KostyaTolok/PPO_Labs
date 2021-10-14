package com.lab2.notes.dao

import androidx.room.*
import com.lab2.notes.entities.Note

@Dao
interface NoteDao {
    @Query("select * from notes order by date_time desc")
    suspend fun getAllNotes(): List<Note>

    @Query("select * from notes where id = :id")
    suspend fun getNoteById(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}