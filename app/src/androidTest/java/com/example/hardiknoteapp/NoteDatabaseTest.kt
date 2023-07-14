package com.example.hardiknoteapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hardiknoteapp.data.db.note.AppRoomDatabase
import com.example.hardiknoteapp.data.db.note.NotesDao
import com.example.hardiknoteapp.data.modal.NoteModal
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDatabaseTest : TestCase() {

    private lateinit var db: AppRoomDatabase
    private lateinit var dao: NotesDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase::class.java).build()
        dao = db.notesDao()
    }


    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertNoteToDBCheck() = runBlocking {
        val note = NoteModal(1, System.currentTimeMillis(), "Test", "Test desc")
        dao.insertNote(note)
        val noteList = dao.getAllNotes()
        assertThat(noteList.contains(note)).isTrue()
    }

    @Test
    fun updateNoteToDBCheck() = runBlocking {
        val note = NoteModal(1, System.currentTimeMillis(), "Test", "Test desc")
        dao.insertNote(note)

        val updatedNote =
            NoteModal(1, System.currentTimeMillis(), "TestUpdated", "Test desc updated")
        dao.updateNote(updatedNote)

        val result = dao.getSingleNote(updatedNote.noteId)

        assertThat(result.title).isEqualTo(updatedNote.title)

    }

    @Test
    fun deleteNoteToDBCheck() = runBlocking {
        val note = NoteModal(1, System.currentTimeMillis(), "Test", "Test desc")
        dao.deleteNote(note)
        val noteList = dao.getAllNotes()
        assertThat(noteList.isEmpty()).isTrue()
    }
}