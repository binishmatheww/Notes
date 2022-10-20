package com.binishmatheww.notes.views.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.themes.AppTheme
import com.binishmatheww.notes.core.themes.ColorPalette
import com.binishmatheww.notes.core.utilities.networkManagers.ConnectivityObserver
import com.binishmatheww.notes.core.utilities.noRippleClickable
import com.binishmatheww.notes.core.widgets.NotesWidget
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.viewModels.HomeViewModel
import com.binishmatheww.notes.views.composables.AddNoteDialog
import com.binishmatheww.notes.views.composables.NotePreviewCard
import com.binishmatheww.notes.views.composables.TextInputField
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNoteClick: (Long) -> Unit,
) {

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

        val context = LocalContext.current

        val scope = rememberCoroutineScope()

        var openAddNoteDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .requiredHeight(100.dp)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = context.getString(R.string.spacedAppName),
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary, shadow = Shadow(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            offset = Offset(4.0f, 8.0f),
                            blurRadius = 3f
                        )
                    )
                )

            }

            Row(
                modifier = Modifier.fillMaxHeight(0.9f)
            ) {

                val notes by viewModel.getNotes()
                    .collectAsStateWithLifecycle(initialValue = arrayListOf())

                if (notes.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .align(Alignment.Center)
                                .noRippleClickable {
                                    openAddNoteDialog = true
                                }, horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                modifier = Modifier
                                    .size(75.dp)
                                    .alpha(0.5F),
                                painter = painterResource(id = R.drawable.note),
                                contentDescription = context.getString(R.string.appName),
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (viewModel.searchQuery.isBlank()) context.getString(R.string.addNotesAndTheyWillBeShownHere) else context.getString(
                                    R.string.nothingMatchesYourQuery
                                ), textAlign = TextAlign.Center, style = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 16.sp
                                )
                            )

                        }

                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(16.dp)
                    ) {

                        items(notes) { note ->

                            NotePreviewCard(modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                                noteId = note.id,
                                noteColorId = note.colorId,
                                noteTitle = note.title,
                                onNoteClick = {
                                    openAddNoteDialog = false
                                    onNoteClick.invoke(it)
                                },
                                onNoteDelete = {
                                    viewModel.deleteNoteById(it)
                                    Toast.makeText(
                                            context,
                                            context.getText(R.string.deletedTheNote),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                })

                        }

                    }

                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .requiredHeight(100.dp)
                    .padding(
                        start = 10.dp, end = 10.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextInputField(modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    )
                    .requiredHeight(50.dp)
                    .fillMaxWidth()
                    .weight(
                        weight = 7.5f
                    ),
                    text = viewModel.searchQuery,
                    onValueChange = {
                        viewModel.searchQuery = it
                    },
                    imeAction = ImeAction.Done,
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                    placeHolderTitle = context.getString(R.string.searchHere),
                    onDoneClick = {

                    })

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                        .weight(
                            weight = 0.5f
                        )
                )

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(
                            weight = 2f
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.large
                        )
                        .clickable(onClick = {
                            openAddNoteDialog = true
                        }), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(28.dp)
                    )
                }

            }

        }

        if (openAddNoteDialog) {

            AddNoteDialog(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
                onDismiss = {
                    openAddNoteDialog = false
                },
                onSave = { title, description ->

                    openAddNoteDialog = false

                    val id = System.currentTimeMillis()

                    val note = Note(
                        id, title, description, (0..5).random(), id
                    )

                    viewModel.addNote(note)

                    Toast.makeText(
                        context, context.getText(R.string.savedTheNote), Toast.LENGTH_SHORT
                    ).show()

                    scope.launch {
                        NotesWidget.notifyNotesWidget(
                            context = context,
                            noteTitle = title
                        )
                    }

                })

        }

    }


}