package com.binishmatheww.notes.models.databases
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.dataAccessObjects.NoteAccessObject

@Database(entities = [Note::class], version = 1,exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun notes() : NoteAccessObject

    companion object {
        @Volatile private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }

        fun buildDatabase(context: Context) = Room
            .databaseBuilder(context, NoteDatabase::class.java, "notes")
            .build()
    }
}