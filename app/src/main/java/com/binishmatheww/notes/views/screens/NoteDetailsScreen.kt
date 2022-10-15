package com.binishmatheww.notes.views.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.viewModels.NoteDetailsViewModel
import com.binishmatheww.notes.views.composables.TextInputField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NoteDetailsScreen(
    noteId : Long,
    noteDetailsViewModel : NoteDetailsViewModel = hiltViewModel()
){

    Theme.NotesTheme {

        val verticalScrollState = rememberScrollState()

        SelectionContainer(
            modifier = Modifier.fillMaxSize()
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(verticalScrollState)
            ){

                val note by noteDetailsViewModel.getNoteById(noteId).collectAsState(initial = null)

                var title by remember { mutableStateOf("") }

                var description by remember { mutableStateOf("") }

                LaunchedEffect(
                    key1 = true,
                    block = {

                        launch(Dispatchers.IO) {

                            while (note == null){
                                delay(100)
                            }

                            withContext(Dispatchers.Main){
                                note?.let {
                                    title = it.title
                                    description = it.description
                                }
                            }

                        }

                    }
                )

                val ( noteSaveButtonConstraint, noteTitleConstraint, noteDescriptionConstraint ) = createRefs()

                val alpha = remember { Animatable(0f) }

                LaunchedEffect(
                    key1 = true,
                    block = {
                        alpha.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = 1000,
                            )
                        )
                    }
                )

                Button(
                    modifier = Modifier.constrainAs(noteSaveButtonConstraint) {
                        top.linkTo(parent.top, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    },
                    onClick = {
                        if(title.isNotBlank()){

                            noteDetailsViewModel.addNote(
                                Note(
                                    id = noteId,
                                    title = title,
                                    description = description,
                                    colorId = note?.colorId ?: 0,
                                    modifiedAt = System.currentTimeMillis()
                                )
                            )

                        }
                    }
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.save),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                TextInputField(
                    text = title,
                    modifier = Modifier
                        .constrainAs(noteTitleConstraint) {
                            top.linkTo(noteSaveButtonConstraint.bottom, 64.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .alpha(alpha.value),
                    placeHolderTitle = LocalContext.current.getString(R.string.enterNoteTitle),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary),
                    onValueChange = {
                        title = it
                    }
                )

                TextInputField(
                    text = description,
                    modifier = Modifier
                        .constrainAs(noteDescriptionConstraint) {
                            top.linkTo(noteTitleConstraint.bottom, 32.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .alpha(alpha.value),
                    placeHolderTitle = LocalContext.current.getString(R.string.enterNoteDescription),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                    onValueChange = {
                        description = it
                    }
                )

            }

        }

    }

}