package com.binishmatheww.notes.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.core.utilities.onSwipe
import com.binishmatheww.notes.core.utilities.toDate

@Composable
fun NotePreviewCard(
    modifier : Modifier = Modifier,
    noteId : Long = 0,
    noteColorId : Int = 0,
    noteTitle : String = "Sample title",
    onNoteClick : (Long) -> Unit = {},
    onNoteDelete : (Long) -> Unit = {},
){

    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .defaultMinSize(
                minHeight = 100.dp,
                minWidth = 200.dp
            )
            .clickable {
                onNoteClick.invoke(noteId)
            }
            .onSwipe(
                key = noteTitle,
                onSwipeLeft = {
                    onNoteDelete.invoke(noteId)
                    true
                },
                onSwipeRight = {
                    onNoteDelete.invoke(noteId)
                    true
                }
            )
    ){

        ConstraintLayout(
            modifier = Modifier
                .background(
                    Theme.ColorPalette
                        .getColorByNumber(noteColorId)
                        .copy(alpha = 0.6f)
                )
                .padding(horizontal = 16.dp)
        ) {

            val ( titleConstraint, dateConstraint ) = createRefs()

            Text(
                modifier = Modifier.constrainAs(titleConstraint){
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(dateConstraint.top, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                text = noteTitle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Text(
                modifier = Modifier.constrainAs(dateConstraint){
                    bottom.linkTo(parent.bottom, 8.dp)
                    end.linkTo(parent.end, 4.dp)
                },
                text = LocalContext.current.getString(R.string.addedAt).plus(" ").plus(noteId.toDate()),
                style = MaterialTheme.typography.labelMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

        }

    }

}