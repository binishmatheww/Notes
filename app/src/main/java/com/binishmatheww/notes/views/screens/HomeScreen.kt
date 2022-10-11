package com.binishmatheww.notes.views.screens

import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.core.utilities.noRippleClickable
import com.binishmatheww.notes.core.utilities.observeAsSate
import com.binishmatheww.notes.core.utilities.onSwipe
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.viewModels.HomeViewModel
import com.binishmatheww.notes.views.composables.AddNoteDialog
import com.binishmatheww.notes.views.composables.ImageButton
import com.binishmatheww.notes.views.composables.TextInputField

@Composable
fun HomeScreen(
    homeViewModel : HomeViewModel = hiltViewModel()
) {

    Theme.NotesTheme {

        val context = LocalContext.current.applicationContext

        val lifeCycleState = LocalLifecycleOwner.current.lifecycle.observeAsSate().value

        val openAddNoteDialog = remember { mutableStateOf(false) }


        if ( lifeCycleState == Lifecycle.Event.ON_PAUSE ) {
            openAddNoteDialog.value = false
        }

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

                Text(text = LocalContext.current.getString(R.string.spaced_app_name),
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth(),
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
            ) {

                DisplayNotes(
                    homeViewModel = homeViewModel,
                    addNote = {
                        openAddNoteDialog.value = true
                    },
                    onNoteClicked = {
                        openAddNoteDialog.value = false
                    },
                    onNoteDeleted = {
                        homeViewModel.deleteNoteById(it)
                    })

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .requiredHeight(100.dp)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box( modifier = Modifier.requiredWidth(250.dp)){

                    TextInputField(
                        modifier = Modifier
                            .fillMaxWidth(0.7f),
                        value = homeViewModel.searchQuery,
                        onValueChange = {
                            homeViewModel.searchQuery = it
                        },
                        imeAction = ImeAction.Done,
                        maxLines = 1,
                        textStyle = MaterialTheme.typography.labelLarge.copy( color = MaterialTheme.colorScheme.primary ),
                        placeHolderTitle = "Search here...",
                        onDoneClick = {

                        }
                    )

                }

                ImageButton(
                    modifier = Modifier.size(50.dp),
                    icon = Icons.Outlined.Add,
                    onClick = {
                        openAddNoteDialog.value = true
                    }
                )

            }

        }

        if (openAddNoteDialog.value) {

            AddNoteDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                onDismiss = {
                    openAddNoteDialog.value = false
                },
                onSave = { title, description ->

                    openAddNoteDialog.value = false

                    val id = System.currentTimeMillis()

                    val note = Note(
                        id,
                        title,
                        description,
                        (0..5).random(),
                        id
                    )

                    homeViewModel.addNote(note).invokeOnCompletion {

                        if (it == null) {
                            Toast.makeText(
                                context,
                                "Saved $title",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
            )

        }

    }


}

@Composable
fun DisplayNotes(
    homeViewModel : HomeViewModel,
    addNote : () -> Unit,
    onNoteClicked : (Long) -> Unit,
    onNoteDeleted : (Long) -> Unit
) {

    val starting = remember { Animatable(0f) }

    LaunchedEffect(
        key1 = true,
        block = {

        starting.animateTo(
            1f,
            animationSpec =
            tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )

    })

    val notes by homeViewModel.getNotesByQuery().collectAsState(initial = arrayListOf())

    if(notes.isEmpty()){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .alpha(starting.value),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .align(Alignment.Center)
                    .noRippleClickable(addNote),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .size(75.dp)
                        .alpha(0.5F),
                    painter = painterResource(id = R.drawable.note),
                    contentDescription = "Logo",
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (homeViewModel.searchQuery.isBlank()) "Nothing here..." else "Nothing matches your query...",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary.copy(0.4f),
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (homeViewModel.searchQuery.isBlank()){
                    Text(
                        text = "Add some notes and they will appear here",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary.copy(0.2F),
                            fontSize = 20.sp
                        )
                    )
                }

            }

        }

    }
    else{

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(notes){ note ->

                Card(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable {
                            onNoteClicked.invoke(note.id)
                        }
                        .onSwipe(
                            onSwipeLeft = {
                                onNoteDeleted.invoke(note.id)
                                true
                            },
                            onSwipeRight = {
                                onNoteDeleted.invoke(note.id)
                                true
                            }
                        )
                ){

                    Box(
                        modifier = Modifier
                            .background(
                                Theme.ColorPalette
                                    .getColorByNumber(note.colorId)
                                    .copy(alpha = 0.6f)
                            )
                            .padding(horizontal = 16.dp)
                    ) {


                        Column(modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 30.dp)) {

                            Text(
                                text = note.title,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textAlign = TextAlign.Center
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = note.description,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textAlign = TextAlign.Center
                                )
                            )

                        }


                    }

                }

            }

        }

    }

}