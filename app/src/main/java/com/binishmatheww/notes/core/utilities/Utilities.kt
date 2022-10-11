package com.binishmatheww.notes.core.utilities

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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

//https://github.com/Ahmed-Sellami/List-Animations-In-Compose/blob/swipe-to-delete/app/src/main/java/com/example/listanimationsincompose/ui/SwipeToDelete.kt
fun Modifier.onSwipe(
    onSwipeRight: () -> Boolean,
    onSwipeLeft: () -> Boolean
): Modifier = composed {

    val offsetX = remember { Animatable(0f) }

    val maximumWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() } * 0.5f

    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation of other items.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->

                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        change.consumePositionChange()

                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                var velocity = velocityTracker.calculateVelocity().x
                // Calculate the eventual position where the fling should settle
                // based on the current offset value and velocity
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // Set the upper and lower bounds so that the animation stops when it
                // reaches the edge.
                offsetX.updateBounds(
                    lowerBound = (-size.width.toFloat()),
                    upperBound = size.width.toFloat()
                )
                launch {

                    //  Slide back the element if the settling position does not go beyond
                    //  the size of the element. Remove the element if it does.
                    if (targetOffsetX.absoluteValue <= maximumWidth) {
                        // Not enough velocity; Slide back.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    }
                    else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(
                            // If the velocity is low, we create a fake velocity to make the animation look smoother
                            when (velocity) {
                                in 0f..500f -> {
                                    3000f
                                }
                                in -500f..0f -> {
                                    -3000f
                                }
                                else -> {
                                    velocity
                                }
                            },
                            decay
                        )
                        // The element was swiped away.
                        if(velocity >= 0){
                            if(!onSwipeRight.invoke()){
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            }
                        }
                        else{
                            if (!onSwipeLeft.invoke()){
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            }
                        }
                    }

                }
            }
        }
    }
        .offset {
            // Use the animating offset value here.
            IntOffset(offsetX.value.toInt(), 0)
        }

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