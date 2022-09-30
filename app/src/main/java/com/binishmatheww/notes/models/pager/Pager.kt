package com.binishmatheww.notes.models.pager

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.binishmatheww.notes.core.utilities.log
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.models.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class Pager( private val noteRepository : NoteRepository ){

    companion object{
        private const val TAG = "Pager"
    }

    var notesPerPage = 0

    var notes = mutableStateListOf<Note>()
    private set

    var previousId = mutableStateOf<Long?>(null)
    private set

    var nextId = mutableStateOf<Long?>(null)
    private set

    var isLoading = mutableStateOf(false)
    private set

    suspend fun loadAll( onComplete : (Boolean) -> Unit ) {

        if (isLoading.value) return

        isLoading.value = true

        withContext(IO){

            //TODO

            isLoading.value = false

            onComplete.invoke(true)

        }

    }

    suspend fun loadNext( onComplete : (Boolean) -> Unit ){

        if (isLoading.value) return

        isLoading.value = true

        val next = nextId.value

        withContext(IO){

            val page = noteRepository.getNotesByQueryAfterId("",next, notesPerPage)

            delay(1000)

            withContext(Main){

                previousId.value = page.previousId
                nextId.value = page.nextId

                if(nextId.value != null){

                    notes.clear()
                    notes.addAll(page.notes)

                }

                //log(TAG) { "loaded next ${page.notes.size} notes. total : ${notes.size}, previousId : $previousId, nextId : $nextId" }

                isLoading.value = false

                onComplete.invoke(true)

            }

        }

    }

    suspend fun loadPrevious( onComplete : (Boolean) -> Unit ){

        if (isLoading.value) return

        isLoading.value = true

        val previous = previousId.value

        withContext(IO){

            val page = noteRepository.getNotesByQueryBeforeId("",previous, notesPerPage)

            withContext(Main){

                previousId.value = page.previousId
                nextId.value = page.nextId

                if(previousId.value != null){

                    notes.clear()
                    notes.addAll(page.notes)

                }

                //log(TAG) { "loaded previous ${page.notes.size} notes. total : ${notes.size}, previousId : $previousId, nextId : $nextId" }

                isLoading.value = false

                onComplete.invoke(true)

            }

        }

    }

}