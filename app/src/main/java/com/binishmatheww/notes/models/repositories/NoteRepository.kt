package com.binishmatheww.notes.models.repositories

import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.databases.NoteDatabase
import com.binishmatheww.notes.models.pager.PagedNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor( private val noteDatabase : NoteDatabase ) {

    fun getNotesByQuery( searchQuery : String ) : Flow<List<Note>> {

        return noteDatabase.notes().getNotesByQuery("%${searchQuery}%")

    }

    fun getNotesByQueryAfterId( searchQuery : String, startingIndex : Long?, limit : Int ) : PagedNotes {

        noteDatabase.notes().getNotesByQueryAfterId( start = startingIndex ?: 0L, limit = limit ).let {

            return if( it.isEmpty() ){
                PagedNotes(previousId = startingIndex, emptyList(),null)
            }
            else{

                PagedNotes(previousId = startingIndex,it,it.last().id)
            }

        }

    }

    fun getNotesByQueryBeforeId( searchQuery : String, endingIndex : Long?, limit : Int ) : PagedNotes {

        noteDatabase.notes().getNotesByQueryBeforeId( start = endingIndex ?: 0L, limit = limit ).let {

            return if( it.isEmpty() ){
                PagedNotes(previousId = null, emptyList(),endingIndex)
            }
            else{

                PagedNotes(previousId = it.first().id,it,endingIndex)
            }

        }

    }

    suspend fun getNoteById( id : Long ) : Note? {
        return withContext(Dispatchers.IO) {
            noteDatabase.notes().getNoteById(id)
        }
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