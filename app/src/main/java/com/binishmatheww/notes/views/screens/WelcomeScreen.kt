package com.binishmatheww.notes.views.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme

@Composable
fun WelcomeScreen( onComplete : () -> Unit ) {

    val angle = remember { Animatable(90f) }

    LaunchedEffect(true){
        angle.animateTo(
            -5f,
            animationSpec = tween(
                durationMillis = 2000
            )
        )
        onComplete.invoke()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.ColorPalette.primaryColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = LocalContext.current.getString(R.string.spaced_app_name),
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .graphicsLayer {
                    rotationX = angle.value
                },
            style = Theme.Typography.bold24
        )
    }

}
