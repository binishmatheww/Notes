package com.binishmatheww.notes.views.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.themes.AppTheme
import com.binishmatheww.notes.core.utilities.OnLifeCycleState

@Composable
fun WelcomeScreen(
    onResume : () -> Unit
) {

    AppTheme.NotesTheme {

        val size = remember { Animatable(1f) }

        OnLifeCycleState(
            lifecycleState = Lifecycle.State.RESUMED,
            onState = { onResume.invoke() }
        )

        LaunchedEffect(true){
            size.animateTo(
                2f,
                animationSpec = tween(
                    durationMillis = 2000
                )
            )
            onResume.invoke()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = LocalContext.current.getString(R.string.spacedAppName),
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .graphicsLayer {
                        scaleX = size.value
                        scaleY = size.value
                    },
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        offset = Offset(4.0f, 8.0f),
                        blurRadius = 3f
                    )
                )
            )
        }

    }

}
