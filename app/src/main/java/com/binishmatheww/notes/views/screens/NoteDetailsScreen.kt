package com.binishmatheww.notes.views.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.themes.AppTheme
import com.binishmatheww.notes.core.themes.ColorPalette
import com.binishmatheww.notes.core.utilities.networkManagers.ConnectivityObserver
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.viewModels.NoteDetailsViewModel
import com.binishmatheww.notes.views.composables.TextInputField
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun NoteDetailsScreen(
    noteId : Long,
    viewModel : NoteDetailsViewModel = hiltViewModel(),
    onNoteSaved : () -> Unit
){

    AppTheme.NotesTheme {

        val networkStatus by viewModel.networkConnectivityObserver.observe().collectAsState(
            initial = ConnectivityObserver.Status.UnSpecified
        )

        val systemUiController = rememberSystemUiController()

        systemUiController.setStatusBarColor(
            color = if (networkStatus == ConnectivityObserver.Status.Lost || networkStatus == ConnectivityObserver.Status.Unavailable) {
                ColorPalette.md_theme_light_error
            } else {
                MaterialTheme.colorScheme.background
            }, darkIcons = !isSystemInDarkTheme()
        )

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

                val note by viewModel.getNoteById(noteId).collectAsStateWithLifecycle(initialValue = null)

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

                val ( noteTitleConstraint, noteDescriptionConstraint, noteSaveButtonConstraint ) = createRefs()

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


                TextInputField(
                    text = title,
                    modifier = Modifier
                        .constrainAs(noteTitleConstraint) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .heightIn(
                            min = 40.dp,
                            max = 120.dp
                        )
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
                        .heightIn(
                            min = 40.dp,
                            max = 120.dp
                        )
                        .alpha(alpha.value),
                    placeHolderTitle = LocalContext.current.getString(R.string.enterNoteDescription),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
                    onValueChange = {
                        description = it
                    }
                )

                Button(
                    modifier = Modifier.constrainAs(noteSaveButtonConstraint) {
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    },
                    enabled = title.isNotBlank() && (title != note?.title || description != note?.description),
                    onClick = {

                        viewModel.addNote(
                            Note(
                                id = noteId,
                                title = title,
                                description = description,
                                colorId = note?.colorId ?: 0,
                                modifiedAt = System.currentTimeMillis()
                            )
                        )

                        onNoteSaved.invoke()

                    }
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.save),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

            }

        }

    }

}