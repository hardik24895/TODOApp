package com.example.hardiknoteapp.di

import android.content.Context
import androidx.room.Room
import com.example.hardiknoteapp.data.db.note.AppRoomDatabase
import com.example.hardiknoteapp.data.db.note.AppRoomDatabase.Companion.DATABASE_NAME
import com.example.hardiknoteapp.data.db.note.NotesDao
import com.example.hardiknoteapp.data.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            AppRoomDatabase::class.java,
            DATABASE_NAME
        )
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(database: AppRoomDatabase) = database.notesDao()

    @Provides
    @Singleton
    fun provideRepository(noteDao : NotesDao)  = NotesRepository(noteDao)


}