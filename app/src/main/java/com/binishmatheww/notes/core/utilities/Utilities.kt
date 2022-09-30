package com.binishmatheww.notes.core.utilities

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

fun log(lambda: () -> String) = log("Notes", lambda)

fun log(tag: String = "Notes", lambda: () -> String) = Log.wtf( tag, lambda.invoke() )

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }

@Composable
fun Lifecycle.observeAsSate(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsSate.addObserver(observer)
        onDispose {
            this@observeAsSate.removeObserver(observer)
        }
    }
    return state
}

inline fun Modifier.swipeToDelete(
    crossinline onDelete: (Float) -> Unit
) : Modifier = composed {

    var offsetX by remember { mutableStateOf(0f) }

    offset { IntOffset(offsetX.roundToInt(), 0) }

    draggable(
        orientation = Orientation.Horizontal,
        state = rememberDraggableState { delta ->
            offsetX += delta
        }
    )

}

fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

fun LazyListState.isScrolledToTheTop() = layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0

@Composable
fun ChainedSpring() {
    Surface(
        Modifier.fillMaxSize(),
        color = Color.White
    ) {
        var moved by remember { mutableStateOf(false) }

        LaunchedEffect(true) {
            infiniteLoopFlow.collect {
                moved = it % 2 == 0
            }
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            for (i in 0 until 8) {
                Ring(
                    moved = moved,
                    idx = i,
                    size = (16 + 22 * i).dp,
                    strokeWidth = (8..16).random().toFloat()
                )
            }
        }
    }
}

@Composable
private fun Ring(
    idx: Int,
    moved: Boolean,
    size: Dp,
    strokeWidth : Float
) {
    val offset = remember { Animatable(-50f) }

    LaunchedEffect(moved) {
        delay((80 + 40 * idx).toLong())
        offset.animateTo(
            targetValue = if (moved) 50f else -50f,
            animationSpec = spring(
                dampingRatio = 0.52f - 0.002f * idx,
                stiffness = 20f + 10f * idx
            )
        )
    }

    Canvas(
        Modifier
            .size(size)
            .offset(y = (offset.value).dp)
            .graphicsLayer { rotationX = 0f }
    ) {
        drawRing(strokeWidth)
        drawText("The notes")
    }
}

private fun DrawScope.drawRing(strokeWidth: Float) {
    drawArc(
        color = Color.Black,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(strokeWidth)
    )
}

private fun DrawScope.drawText(text : String ) {

    drawContext.canvas.nativeCanvas.apply {
        drawText(
            text,
            size.width / 2,
            size.height / 2,
            Paint().apply {
                textSize = 100F
                color = android.graphics.Color.BLUE
                textAlign = Paint.Align.CENTER
            }
        )
    }

}

private val infiniteLoopFlow: Flow<Int> = flow {
    var idx = 0
    while (true) {
        emit(idx)
        idx++
        delay(2500L)
    }
}