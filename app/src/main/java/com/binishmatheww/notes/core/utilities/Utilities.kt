package com.binishmatheww.notes.core.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.glance.*
import androidx.glance.appwidget.cornerRadius
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.absoluteValue
import com.binishmatheww.notes.R


fun log(lambda: () -> String) = log("Notes", lambda)

fun log(tag: String = "Notes", lambda: () -> String) = Log.wtf(tag, lambda.invoke())

/**
 * Function to convert milliseconds to date in dd/MM/yyyy hh:mm aa format.
 */
fun Long.toDate(): String {

    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault())

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

@Composable
fun OnLifeCycleState(
    key1 : Any? = true,
    lifecycleState : Lifecycle.State,
    onState : suspend () -> Unit
){

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(
        key1 = key1
    ){

        lifecycle.repeatOnLifecycle(
            state = lifecycleState
        ) {
           onState.invoke()
        }

    }

}

// Copied from FlowExt.kt - package androidx.lifecycle.compose
@ExperimentalLifecycleComposeApi
@Composable
fun <T> Flow<T>.collectAsStateWithLifecycleAndCallback(
    initialValue: T,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    callback: suspend (T) -> Unit
): State<T> = collectAsStateWithLifecycleAndCallback(
    initialValue = initialValue,
    lifecycle = lifecycleOwner.lifecycle,
    minActiveState = minActiveState,
    context = context,
    callback = callback
)

// Copied from FlowExt.kt - package androidx.lifecycle.compose
@ExperimentalLifecycleComposeApi
@Composable
fun <T> Flow<T>.collectAsStateWithLifecycleAndCallback(
    initialValue: T,
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
    callback: suspend (T) -> Unit
): State<T> {
    return produceState(initialValue, this, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@collectAsStateWithLifecycleAndCallback.collect {
                    this@produceState.value = it
                    callback.invoke(it)
                }
            } else withContext(context) {
                this@collectAsStateWithLifecycleAndCallback.collect {
                    this@produceState.value = it
                    callback.invoke(it)
                }
            }
        }
    }
}

//https://github.com/Ahmed-Sellami/List-Animations-In-Compose/blob/swipe-to-delete/app/src/main/java/com/example/listanimationsincompose/ui/SwipeToDelete.kt
fun Modifier.onSwipe(
    onSwipeRight: () -> Boolean,
    onSwipeLeft: () -> Boolean
): Modifier = composed {

    val offsetX = remember { Animatable(0f) }

    val threshold = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() } * 0.6f

    pointerInput(System.currentTimeMillis()) {

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

                        if (horizontalDragOffset > 50f) change.consume()

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
                    } else {

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

                        if (velocity >= 0) {
                            if (!onSwipeRight.invoke()) {
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            } else {
                                offsetX.snapTo(0f)
                            }
                        } else {
                            if (!onSwipeLeft.invoke()) {
                                offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                            } else {
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


fun GlanceModifier.cornerRadiusCompat(
    backgroundColor: Color,
    width: Int = 50,
    height: Int = 50,
    cornerRadius: Int = 2
): GlanceModifier {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        this.background(backgroundColor).cornerRadius(cornerRadius.dp)

    } else {

        /*this.background(
            BitmapImageProvider(
                PaintDrawable(backgroundColor.toArgb()).apply {
                    setCornerRadius(cornerRadius.toFloat())
                }.toBitmap(
                    width = width,
                    height = height,
                    config = Bitmap.Config.ARGB_8888
                )
            )
        )*/
        this.background(ImageProvider(R.drawable.dp_5_round_cornered_white_background))

    }

}

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
    strokeWidth: Float
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