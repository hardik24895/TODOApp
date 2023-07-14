package com.example.hardiknoteapp.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class NoteModal(
    @PrimaryKey(autoGenerate = true)
    var noteId:Long =0L,
    var date: Long =System.currentTimeMillis(),
    var title:String,
    var description:String
) :Serializable