package com.binishmatheww.notes.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.binishmatheww.notes.views.fragments.WelcomeScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private var hasBackPressedTwice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, WelcomeScreenFragment(), WelcomeScreenFragment::class.java.simpleName)
            .addToBackStack(WelcomeScreenFragment::class.java.simpleName)
            .commitAllowingStateLoss()

    }

    override fun onBackPressed() {

        if(supportFragmentManager.backStackEntryCount > 1){
            super.onBackPressed()
        }
        else{

            if( hasBackPressedTwice ){
                finish()
            }
            else{

                hasBackPressedTwice = true

                Toast.makeText(this,"Press again to exit",Toast.LENGTH_LONG).show()

                Handler(Looper.getMainLooper()).postDelayed({

                    hasBackPressedTwice = false

                }, 1000)

            }

        }

    }


}
