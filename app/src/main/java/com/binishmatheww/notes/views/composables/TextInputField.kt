package com.binishmatheww.notes.views.composables

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    text: String,
    textStyle : TextStyle = TextStyle(
        color = Color.Black,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    onValueChange: (String) -> Unit,
    placeHolderTitle: String = "",
    imeAction: ImeAction = ImeAction.None,
    maxLines : Int? = null,
    onNextClick: () -> Unit = {},
    onDoneClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeHolderTitle,
                color = textStyle.color.copy(alpha = 0.5f),
                fontSize = textStyle.fontSize,
                fontWeight = textStyle.fontWeight,
                fontFamily = textStyle.fontFamily,
                textAlign = textStyle.textAlign
            )
        },
        textStyle = textStyle,
        colors = TextFieldDefaults.textFieldColors(
            textColor = textStyle.color,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = textStyle.color.copy(alpha = 0.5f)
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
                onNextClick()
            },
            onDone = {
                focusManager.clearFocus()
                onDoneClick()
            }
        ),
        maxLines = maxLines ?: Int.MAX_VALUE,
        readOnly = false,
        enabled = true
    )
}