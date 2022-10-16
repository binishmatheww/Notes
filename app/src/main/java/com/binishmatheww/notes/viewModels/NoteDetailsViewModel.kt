package com.binishmatheww.notes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    fun getNoteById( id : Long ) = noteRepository.getNoteById(id = id)

    fun addNote( note: Note ) = viewModelScope.launch { noteRepository.addNote(note) }

}