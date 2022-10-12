package com.binishmatheww.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.binishmatheww.notes.core.constants.Routes
import com.binishmatheww.notes.views.screens.HomeScreen
import com.binishmatheww.notes.views.screens.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(400)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        setContent {

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
