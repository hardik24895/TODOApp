package com.example.hardiknoteapp.data.db.note
import androidx.room.*
import com.example.hardiknoteapp.data.modal.NoteModal

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: NoteModal)

    @Query("SELECT * from notemodal ORDER BY date DESC")
   suspend fun getAllNotes(): MutableList<NoteModal>

    @Delete
    suspend fun deleteNote(note: NoteModal)

    @Update
    suspend fun updateNote(note: NoteModal)

    @Query("SELECT * from notemodal Where noteId = :id")
    suspend fun getSingleNote(id:Long): NoteModal
}