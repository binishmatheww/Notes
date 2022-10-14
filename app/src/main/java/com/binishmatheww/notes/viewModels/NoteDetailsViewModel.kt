package com.binishmatheww.notes.viewModels

import androidx.lifecycle.ViewModel
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    fun getNoteById( id : Long ) : Flow<Note?> {

        return noteRepository.getNoteById(
            id = id
        )

    }

}