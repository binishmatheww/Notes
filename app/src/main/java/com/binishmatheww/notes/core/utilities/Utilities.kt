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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

fun log(lambda: () -> String) = log("Notes", lambda)

fun log(tag: String = "Notes", lambda: () -> String) = Log.wtf( tag, lambda.invoke() )

/**
 * Function to convert milliseconds to date in dd/MM/yyyy hh:mm:ss.SSS format.
 */
fun Long.toDate() : String {

    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm aa")

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        formatter.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}

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

    val threshold = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() } * 0.6f

    pointerInput(this) {

        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {

                val pointerId = awaitPointerEventScope { awaitFirstDown().id }

                offsetX.stop()

                val velocityTracker = VelocityTracker()

                awaitPointerEventScope {

                    horizontalDrag(pointerId) { change ->

                        val horizontalDragOffset = offsetX.value + change.positionChange().x

                        launch {
                            offsetX.snapTo(horizontalDragOffset)
                        }

                        velocityTracker.addPosition(change.uptimeMillis, change.position)

                        if ( horizontalDragOffset > 50f ) change.consume()

                    }

                }

                val velocity = velocityTracker.calculateVelocity().x

                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                offsetX.updateBounds(
                    lowerBound = (-size.width.toFloat()),
                    upperBound = size.width.toFloat()
                )

                launch {

                    if (targetOffsetX.absoluteValue <= threshold) {
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    }
                    else {

                        offsetX.animateDecay(
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

                        if(velocity >= 0){
                            if(!onSwipeRight.invoke()){
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            }else{
                                offsetX.snapTo(0f)
                            }
                        }
                        else{
                            if (!onSwipeLeft.invoke()){
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            }else{
                                offsetX.snapTo(0f)
                            }
                        }

                    }

                }
            }
        }
    }
        .offset {
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

        drawArc(
            color = Color.Black,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = Stroke(strokeWidth)
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "The notes",
                this@Canvas.size.width / 2,
                this@Canvas.size.height / 2,
                Paint().apply {
                    textSize = 100F
                    color = android.graphics.Color.BLUE
                    textAlign = Paint.Align.CENTER
                }
            )
        }
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