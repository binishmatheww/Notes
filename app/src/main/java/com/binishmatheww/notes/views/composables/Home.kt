package com.binishmatheww.notes.views.composables

import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.core.utilities.noRippleClickable
import com.binishmatheww.notes.core.utilities.observeAsSate
import com.binishmatheww.notes.models.Note
import com.binishmatheww.notes.viewModels.HomeViewModel
import kotlin.math.roundToInt


@Composable
fun Home(
    lifeCycleState : Lifecycle.Event,
    homeViewModel : HomeViewModel
) {

    val context = LocalContext.current.applicationContext

    val openAddNoteDialog = remember { mutableStateOf(false) }


    if (lifeCycleState == Lifecycle.Event.ON_PAUSE) {
        openAddNoteDialog.value = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

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
                    .background(Theme.ColorPalette.primaryColor)
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
                        textStyle = Theme.Typography.h6,
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



    }

    if (openAddNoteDialog.value) {

        AddNoteDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.ColorPalette.primaryColor),
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

                    //homeViewModel.loadNext {  }

                }

            }
        )

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

    val width = LocalConfiguration.current.screenWidthDp

    LaunchedEffect(key1 = true, block = {

        starting.animateTo(
            1f,
            animationSpec =
            tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )

    })

    val notes = homeViewModel.getNotesByQuery().collectAsState(initial = arrayListOf())

    if(notes.value.isEmpty()){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.ColorPalette.primaryColor)
                .alpha(starting.value),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.ColorPalette.primaryColor)
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
                        color = Theme.ColorPalette.secondaryColor.copy(0.4f),
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (homeViewModel.searchQuery.isBlank()){
                    Text(
                        text = "Add some notes and they will appear here",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Theme.ColorPalette.primaryColor.copy(0.2F),
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
                .background(Theme.ColorPalette.primaryColor),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(notes.value){ note ->

                var offsetX by remember { mutableStateOf(0f) }

                Card(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .offset { IntOffset(offsetX.roundToInt(), 0) }
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->

                                offsetX += delta

                            },
                            onDragStopped = {

                                offsetX = if (offsetX > (width / 1.2)) {

                                    onNoteDeleted.invoke(note.id)
                                    0f

                                } else {

                                    0f

                                }

                            }
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                Theme.ColorPalette
                                    .getColorByNumber(note.colorId)
                                    .copy(alpha = 0.6f)
                            )
                            .clickable {
                                onNoteClicked.invoke(note.id)
                            }
                    ) {


                        Column(modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 30.dp)) {

                            Text(
                                text = note.title,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = Theme.Typography.bold14.copy(
                                    textAlign = TextAlign.Center
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = note.description,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                style = Theme.Typography.bold14.copy(
                                    textAlign = TextAlign.Center
                                )
                            )

                        }


                    }

                }

            }

        }

    }


    /*when {

        homeViewModel.pager.isLoading.value -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.ColorPalette.primaryColor)
                    .alpha(starting.value),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = Theme.ColorPalette.secondaryColor
                )

            }

        }

        homeViewModel.pager.notes.isEmpty() -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.ColorPalette.primaryColor)
                    .alpha(starting.value),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.ColorPalette.primaryColor)
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
                            color = Theme.ColorPalette.primaryColor.copy(0.4F),
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (homeViewModel.searchQuery.isBlank()){
                        Text(
                            text = "Add some notes and they will appear here",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Theme.ColorPalette.primaryColor.copy(0.2F),
                                fontSize = 20.sp
                            )
                        )
                    }

                }

            }

        }

        else -> {

            val lazyColumnState = rememberLazyListState()

            val isScrolledToTheTop by remember { derivedStateOf { lazyColumnState.isScrolledToTheTop() } }

            val isScrolledToTheEnd by remember { derivedStateOf { lazyColumnState.isScrolledToTheEnd() } }

            var anchor by remember { mutableStateOf<Long?>(null) }


            if( lazyColumnState.isScrollInProgress ){

                LaunchedEffect( isScrolledToTheEnd ){

                    if( homeViewModel.pager.nextId.value != null && homeViewModel.pager.nextId.value != anchor ){
                        log { "nextId : ${homeViewModel.pager.nextId.value}" }
                        anchor = homeViewModel.pager.nextId.value
                        homeViewModel.loadNext()
                    }

                }

                LaunchedEffect( isScrolledToTheTop ){

                    if( homeViewModel.pager.previousId.value != null && homeViewModel.pager.previousId.value != anchor ){
                        log { "previousId : ${homeViewModel.pager.previousId.value}" }
                        anchor = homeViewModel.pager.previousId.value
                        //homeViewModel.loadPrevious()
                    }

                }

            }

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.ColorPalette.primaryColor),
                contentPadding = PaddingValues(16.dp)
            ) {

                items(homeViewModel.pager.notes){ note ->

                    var offsetX by remember { mutableStateOf(0f) }

                    Card(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .offset { IntOffset(offsetX.roundToInt(), 0) }
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->

                                    offsetX += delta

                                },
                                onDragStopped = {

                                    offsetX = if (offsetX > (width / 1.2)) {

                                        //onNoteDeleted.invoke(note.id)
                                        0f

                                    } else {

                                        0f

                                    }

                                }
                            )
                    ) {

                        Box(
                            modifier = Modifier
                                .background(Theme.ColorPalette.getColorByNumber(note.colorId).copy(alpha = 0.6f))
                                .clickable {
                                    onNoteClicked.invoke(note.id)
                                }
                        ) {


                            Column(modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 30.dp)) {

                                Text(
                                    text = note.title,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        color = Theme.ColorPalette.primaryColor,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = note.description,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        color = Theme.ColorPalette.primaryColor,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontSize = 14.sp
                                )

                            }


                        }

                    }

                }

            }

        }

    }*/


}