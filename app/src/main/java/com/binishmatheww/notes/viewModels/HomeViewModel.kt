package com.binishmatheww.notes.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    var searchQuery by mutableStateOf("")

    fun getNotesByQuery() : Flow<List<Note>>{

        return noteRepository.getNotesByQuery(searchQuery = searchQuery)

    }

    fun addNote( note : Note ) = viewModelScope.launch { noteRepository.addNote(note) }

    fun deleteNoteById( id : Long ) = viewModelScope.launch { noteRepository.deleteNoteById(id) }

}