package com.binishmatheww.notes.models.databases
import android.content.Context
import android.os.Looper
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binishmatheww.notes.core.utilities.log
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.dataAccessObjects.NoteAccessObject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
            /*.setQueryCallback(
                { sqlQuery, bindArgs ->
                    log("Notes Database") { "query : $sqlQuery, args : $bindArgs" }
                },
                Executors.newSingleThreadExecutor()
            )*/
            .build()
    }
}