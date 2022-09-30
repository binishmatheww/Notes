package com.binishmatheww.notes.core

import android.app.Application
import com.binishmatheww.notes.models.databases.NoteDatabase
import com.binishmatheww.notes.models.repositories.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesInjector {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return NoteDatabase.buildDatabase(app)
    }


    @Provides
    @Singleton
    fun provideNoteRepository( noteDatabase: NoteDatabase ): NoteRepository {
        return NoteRepository(noteDatabase)
    }

}