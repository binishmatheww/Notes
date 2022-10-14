package com.binishmatheww.notes.views.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.viewModels.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    noteId : Long,
    noteDetailsViewModel : NoteDetailsViewModel = hiltViewModel()
){

    Theme.NotesTheme {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){

            val note by noteDetailsViewModel.getNoteById(noteId).collectAsState(initial = null)

            val context = LocalContext.current

            val ( appNameConstraint, noteTitleConstraint, noteDescriptionConstraint ) = createRefs()

            Row(
                modifier = Modifier
                    .constrainAs(appNameConstraint) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
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
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
                )

            }

            val alpha = remember { Animatable(0f) }

            LaunchedEffect(
                key1 = true,
                block = {
                    alpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 500,
                        )
                    )
                }
            )

            Text(
                text = note?.title ?: "",
                modifier = Modifier
                    .constrainAs(noteTitleConstraint) {
                        top.linkTo(appNameConstraint.bottom, 20.dp)
                        start.linkTo(parent.start, 15.dp)
                    }
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .alpha(alpha.value),
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary)
            )

            Text(
                text = note?.description ?: "",
                modifier = Modifier
                    .constrainAs(noteDescriptionConstraint) {
                        top.linkTo(noteTitleConstraint.bottom, 20.dp)
                        start.linkTo(parent.start, 15.dp)
                    }
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .alpha(alpha.value),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )

        }

    }

}