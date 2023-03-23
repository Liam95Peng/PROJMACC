package com.example.projmacc

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.projmacc.Game.gameobject.Constants

class MainActivity : AppCompatActivity() {

    val constants: Constants ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity..kt" , "onCreate()")
        super.onCreate(savedInstanceState)
        var dm = window.windowManager.currentWindowMetrics

        constants?.SCREEN_HEIGHT = dm.bounds.height()
        constants?.SCREEN_WIDTH =dm.bounds.width()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }
    /*
    override fun onBackPressed() {
        super.onBackPressed()
    }*/
    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onStart() {
        Log.d("MainActivity.kt" , "onStart()")
        super.onStart()

    }
    /*
    override fun onPause() {
        Log.d("MainActivity.kt" , "onPause()")
        super.onPause()
    }*/

    override fun onStop() {
        Log.d("MainActivity.kt" , "onStop()")
        super.onStop()
    }


    override fun onResume() {
        Log.d("MainActivity.kt" , "onDestroy()")
        super.onResume()
    }
    override fun onDestroy() {
        Log.d("MainActivity.kt" , "onDestroy()")
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}