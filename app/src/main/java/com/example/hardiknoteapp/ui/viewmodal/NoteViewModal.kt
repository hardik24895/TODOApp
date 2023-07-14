package com.example.hardiknoteapp.ui.viewmodal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hardiknoteapp.data.modal.NoteModal
import com.example.hardiknoteapp.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModal @Inject constructor(private val notesRepository: NotesRepository) :ViewModel() {

    private val _noteEvent = MutableSharedFlow<NoteEvent>()

    val  noteEvent = _noteEvent.asSharedFlow()

    private val _noteListEvent = MutableLiveData<NoteEvent>()

    val noteListEvent get() = _noteListEvent

    fun insertNote(note:NoteModal){
        viewModelScope.launch {
            try {
                notesRepository.insertNote(note)
                _noteEvent.emit(NoteEvent.OnSuccessMsg("Note Saved..."))
            }catch ( e:Exception){
                e.message?.let {
                    _noteEvent.emit(NoteEvent.OnError(it))
                }
            }

        }
    }
    fun updateNote(note:NoteModal){
        viewModelScope.launch {
            try {
                notesRepository.updateNote(note)
                _noteEvent.emit(NoteEvent.OnSuccessMsg("Note Updated..."))
            }catch ( e:Exception){
                e.message?.let {
                    _noteEvent.emit(NoteEvent.OnError(it))
                }
            }

        }
    }

    fun deleteNote(note:NoteModal){
        viewModelScope.launch {
            try {
                notesRepository.deleteNote(note)
                _noteEvent.emit(NoteEvent.OnSuccessMsg("Note Deleted..."))
            }catch ( e:Exception){
                e.message?.let {
                    _noteEvent.emit(NoteEvent.OnError(it))
                }
            }

        }
    }


    fun getAllNotes() {
        viewModelScope.launch {
            try {
                _noteListEvent.postValue(NoteEvent.OnSuccessList(notesRepository.getAllNote()))
            } catch (e: Exception) {
                e.message?.let {
                    _noteListEvent.postValue(NoteEvent.OnError(it))
                }
            }
        }
    }

    sealed class NoteEvent(){
        data class OnError(val error: String) : NoteEvent()
        data class OnSuccessMsg(val response: String) : NoteEvent()
        data class OnSuccessList(val response: MutableList<NoteModal>) : NoteEvent()
    }
}