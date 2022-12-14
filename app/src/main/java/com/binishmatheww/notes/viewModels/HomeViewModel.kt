package com.binishmatheww.notes.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binishmatheww.notes.core.utilities.networkManagers.ConnectivityObserver
import com.binishmatheww.notes.core.utilities.networkManagers.NetworkConnectivityObserver
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val noteRepository: NoteRepository,
    val networkConnectivityObserver: ConnectivityObserver,
    ) : ViewModel() {

    var searchQuery by mutableStateOf("")

    fun getNotes() = noteRepository.getNotesByQuery(searchQuery = searchQuery)

    fun addNote( note : Note ) = viewModelScope.launch { noteRepository.addNote(note) }

    fun deleteNoteById( id : Long ) = viewModelScope.launch { noteRepository.deleteNoteById(id) }

}