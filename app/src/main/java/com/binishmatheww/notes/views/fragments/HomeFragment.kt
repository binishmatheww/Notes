package com.binishmatheww.notes.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.core.utilities.observeAsSate
import com.binishmatheww.notes.viewModels.HomeViewModel
import com.binishmatheww.notes.views.composables.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(inflater.context).apply {
            setContent {

                val lifeCycleState = lifecycle.observeAsSate().value

                Theme.NotesTheme {

                    Home(
                        lifeCycleState = lifeCycleState,
                        homeViewModel = homeViewModel
                    )

                }

            }
        }

    }


}