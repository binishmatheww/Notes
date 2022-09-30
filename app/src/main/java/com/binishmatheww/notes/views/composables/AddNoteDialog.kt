package com.binishmatheww.notes.views.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.binishmatheww.notes.core.Theme

@Composable
fun AddNoteDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {

    val context = LocalContext.current.applicationContext

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),

    ) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
        ) {

            Column(
                modifier = modifier,
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.ColorPalette.secondaryColor.copy(alpha = 0.05f))
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Add a note",
                        modifier = Modifier
                            .padding(top = 12.dp),
                        style = Theme.Typography.bold14,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Title",
                    modifier = Modifier.padding(start = 14.dp),
                    style = Theme.Typography.bold14
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp),
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    },
                    imeAction = ImeAction.Next,
                    textStyle = Theme.Typography.bold14,
                    placeHolderTitle = "Note title...",
                    onNextClick = {

                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Description",
                    modifier = Modifier.padding(start = 14.dp),
                    style = Theme.Typography.bold14
                )

                Spacer(modifier = Modifier.height(5.dp))

                TextInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp),
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    imeAction = ImeAction.Done,
                    textStyle = Theme.Typography.bold14,
                    placeHolderTitle = "Note description...",
                    onDoneClick = {

                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier
                            .clickable {
                                if ( title.value.isNotBlank() ) {
                                    onSave.invoke(title.value, description.value)
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "Enter something",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            },
                        style = Theme.Typography.bold20.copy(
                                color = if ( title.value.isNotBlank() ) Theme.ColorPalette.secondaryColor else Theme.ColorPalette.secondaryColor.copy(alpha = 0.5f)
                        )
                    )
                }

            }

        }

    }

}