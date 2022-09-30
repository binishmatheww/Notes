package com.binishmatheww.notes.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.binishmatheww.notes.R
import com.binishmatheww.notes.core.Theme

class WelcomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(inflater.context).apply {
            setContent {

                Theme.NotesTheme {
                    WelcomeScreen {
                        activity?.supportFragmentManager?.apply {
                            popBackStack()
                            beginTransaction()
                            .replace(android.R.id.content, HomeFragment(), HomeFragment::class.java.simpleName)
                            .addToBackStack(HomeFragment::class.java.simpleName)
                            .commitAllowingStateLoss()
                        }
                    }
                }

            }
        }

    }

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
            Text(text = getString(R.string.spaced_app_name),
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

}