package com.binishmatheww.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binishmatheww.notes.core.constants.Routes
import com.binishmatheww.notes.core.Theme
import com.binishmatheww.notes.views.screens.HomeScreen
import com.binishmatheww.notes.views.screens.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Theme.NotesTheme {

                val navController = rememberNavController()

                NavHost( navController = navController, startDestination = Routes.welcomeScreen ){

                    composable( route = Routes.welcomeScreen ){

                        WelcomeScreen{
                            navController.popBackStack()
                            navController.navigate(Routes.homeScreen)
                        }

                    }

                    composable( route = Routes.homeScreen ){

                        HomeScreen()

                    }

                }

            }

        }

    }


}
