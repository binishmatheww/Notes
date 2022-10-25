package com.binishmatheww.notes.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.binishmatheww.notes.core.constants.Routes
import com.binishmatheww.notes.views.screens.HomeScreen
import com.binishmatheww.notes.views.screens.NoteDetailsScreen
import com.binishmatheww.notes.views.screens.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private var noteId : Long? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        noteId = intent?.extras?.getLong("id")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(400)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        noteId = intent?.extras?.getLong("id")

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Routes.welcomeScreen,
            ){

                fun navigateToNoteIfExists(){

                    if( noteId != null && noteId != 0L ){

                        if(navController.currentDestination?.route == Routes.noteDetailsScreen.plus("/{id}")){
                            navController.popBackStack()
                        }

                        navController.navigate(
                            route = Routes.noteDetailsScreen.plus("/$noteId")
                        )
                        noteId = null

                    }

                }

                composable(
                    route = Routes.welcomeScreen
                ){

                    WelcomeScreen(
                        onResume = {

                            navController.popBackStack()

                            navController.navigate(Routes.homeScreen)

                            navigateToNoteIfExists()

                        }
                    )

                }

                composable(
                    route = Routes.homeScreen
                ){

                    HomeScreen(
                        onResume = {

                            navigateToNoteIfExists()

                        },
                        onNoteClick = {
                            navController.navigate(
                                route = Routes.noteDetailsScreen.plus("/$it")
                            )
                        }
                    )

                }

                composable(
                    route = Routes.noteDetailsScreen.plus("/{id}"),
                    arguments = listOf(
                        navArgument("id"){
                            this.type = NavType.LongType
                        }
                    )
                ){ backStackEntry ->

                    NoteDetailsScreen(
                        noteId = backStackEntry.arguments?.getLong("id") ?: 0,
                        onResume = {
                            navigateToNoteIfExists()
                        },
                        onNoteSaved = {
                            navController.popBackStack()
                        }
                    )

                }

            }

        }

    }


}
