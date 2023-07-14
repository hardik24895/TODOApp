package com.example.hardiknoteapp.data.repository

import com.example.hardiknoteapp.data.db.note.NotesDao
import com.example.hardiknoteapp.data.modal.NoteModal
import javax.inject.Inject

class NotesRepository @Inject constructor (private val noteDao: NotesDao) {

    suspend fun getAllNote() = noteDao.getAllNotes()

    suspend fun insertNote(note: NoteModal) = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteModal) = noteDao.updateNote(note)

    suspend fun deleteNote(note: NoteModal) = noteDao.deleteNote(note)

}