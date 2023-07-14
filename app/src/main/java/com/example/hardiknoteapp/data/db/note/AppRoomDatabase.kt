package com.example.hardiknoteapp.data.db.note

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hardiknoteapp.data.modal.NoteModal

@Database(entities = [NoteModal::class], version = 1, exportSchema = false)
 abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "AppRoom.db"
    }

}