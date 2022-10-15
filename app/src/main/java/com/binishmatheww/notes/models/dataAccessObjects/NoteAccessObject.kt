package com.binishmatheww.notes.models.dataAccessObjects

import androidx.room.*
import com.binishmatheww.notes.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteAccessObject {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote( note : Note ) : Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote( note : Note ) : Int

    @Delete
    fun deleteNote( note : Note ) : Int

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteNoteById( id : Long ) : Int

    @Query("DELETE FROM notes WHERE title = :searchQuery OR description = :searchQuery")
    fun deleteNoteBy( searchQuery : String ) : Int

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById( id : Long ) : Flow<Note?>

    @Query("SELECT * FROM notes WHERE id > :start ORDER BY id LIMIT :limit")
    fun getNotesByQueryAfterId( start : Long, limit : Int ) : List<Note>

    @Query("SELECT * FROM notes WHERE id < :start ORDER BY id LIMIT :limit")
    fun getNotesByQueryBeforeId( start : Long, limit : Int ) : List<Note>

    @Query("SELECT * FROM notes WHERE title like :searchQuery OR description like :searchQuery")
    fun getNotesByQuery( searchQuery : String ) : Flow<List<Note>>

    @Query("SELECT * FROM notes")
    fun getNotes() : Flow<List<Note>>


}