package com.binishmatheww.notes.views.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.binishmatheww.notes.core.themes.AppTheme
import com.binishmatheww.notes.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNoteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    AppTheme.NotesTheme {

        val context = LocalContext.current

        val title = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),

            ) {

            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(
                        all = 14.dp
                    ),
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
            ) {

                ConstraintLayout(
                    modifier = modifier,
                ) {

                    val (
                        dialogTitleConstraint,
                        noteTitleConstraint,
                        noteTitleInputConstraint,
                        noteDescriptionConstraint,
                        noteDescriptionInputConstraint,
                        saveButtonConstraint,
                    ) = createRefs()

                    Text(
                        modifier = Modifier
                            .constrainAs(dialogTitleConstraint){
                                top.linkTo(parent.top, 14.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                        text = context.getString(R.string.addNote),
                        style = MaterialTheme.typography.displaySmall.copy( color = MaterialTheme.colorScheme.primary ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(noteTitleConstraint){
                                top.linkTo(dialogTitleConstraint.bottom, 8.dp)
                                start.linkTo(parent.start, 14.dp)
                            },
                        text = context.getString(R.string.title),
                        style = MaterialTheme.typography.labelLarge.copy( color = MaterialTheme.colorScheme.primary )
                    )

                    TextInputField(
                        modifier = Modifier
                            .constrainAs(noteTitleInputConstraint){
                                top.linkTo(noteTitleConstraint.bottom, 8.dp)
                                start.linkTo(parent.start, 14.dp)
                                end.linkTo(parent.end, 14.dp)
                            }
                            .heightIn(
                                min = 40.dp,
                                max = 120.dp
                            ),
                        text = title.value,
                        onValueChange = {
                            title.value = it
                        },
                        //imeAction = ImeAction.Next,
                        textStyle = MaterialTheme.typography.labelLarge.copy( color = MaterialTheme.colorScheme.primary ),
                        placeHolderTitle = context.getString(R.string.enterNoteTitle),
                        onNextClick = {

                        }
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(noteDescriptionConstraint){
                                top.linkTo(noteTitleInputConstraint.bottom, 8.dp)
                                start.linkTo(parent.start, 14.dp)
                            },
                        text = context.getString(R.string.description),
                        style = MaterialTheme.typography.labelLarge.copy( color = MaterialTheme.colorScheme.primary )
                    )

                    TextInputField(
                        modifier = Modifier
                            .constrainAs(noteDescriptionInputConstraint){
                                top.linkTo(noteDescriptionConstraint.bottom, 8.dp)
                                start.linkTo(parent.start, 14.dp)
                                end.linkTo(parent.end, 14.dp)
                            }
                            .heightIn(
                                min = 40.dp,
                                max = 120.dp
                            ),
                        text = description.value,
                        onValueChange = {
                            description.value = it
                        },
                        //imeAction = ImeAction.Done,
                        textStyle = MaterialTheme.typography.labelLarge.copy( color = MaterialTheme.colorScheme.primary ),
                        placeHolderTitle = context.getString(R.string.enterNoteDescription),
                        onDoneClick = {

                        }
                    )

                    Button(
                        modifier = Modifier.constrainAs(saveButtonConstraint){
                            top.linkTo(noteDescriptionInputConstraint.bottom, 8.dp)
                            bottom.linkTo(parent.bottom, 14.dp)
                            end.linkTo(parent.end, 14.dp)
                        },
                        onClick = {

                            if ( title.value.isNotBlank() ) {
                                onSave.invoke(title.value, description.value)
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        context.getText(R.string.enterNoteTitle),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }

                        },
                        content = {

                            Text(
                                text = context.getString(R.string.save),
                                modifier = Modifier,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if ( title.value.isNotBlank() ) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                )
                            )

                        }
                    )

                }

            }

        }

    }

}