package com.binishmatheww.notes.models.repositories

import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.databases.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor( private val noteDatabase : NoteDatabase ) {

    fun getNotesByQuery( searchQuery : String ) : Flow<List<Note>> {

        return noteDatabase.notes().getNotesByQuery("%${searchQuery}%")

    }

    fun getNoteById( id : Long ) : Flow<Note?> {
        return noteDatabase.notes().getNoteById(id)
    }

    suspend fun addNote( note : Note ) {
        withContext(Dispatchers.IO) {
            noteDatabase.notes().addNote(note)
        }
    }

    suspend fun updateNote( note : Note ) {
        withContext(Dispatchers.IO) {
            noteDatabase.notes().updateNote(note)
        }
    }

    suspend fun deleteNote( note : Note ) {
        withContext(Dispatchers.IO) {
            noteDatabase.notes().deleteNote(note)
        }
    }

    suspend fun deleteNoteById( id : Long ) {
        withContext(Dispatchers.IO) {
            noteDatabase.notes().deleteNoteById(id)
        }
    }

}