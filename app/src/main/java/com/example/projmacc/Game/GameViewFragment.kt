package com.example.projmacc.Game

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projmacc.Game.gamepanel.Score
import com.example.projmacc.R
import com.google.android.material.internal.ContextUtils


class GameViewFragment : Fragment() {



    var gameView : GameView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE



        Log.d("TAG GAME FRAGMENT " , "fragment started")


        val dm = activity?.windowManager?.currentWindowMetrics
        gameView = GameView(context,dm!!.bounds.height() , dm.bounds.width())

        return gameView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback( viewLifecycleOwner , object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                if (isEnabled) {
                    Log.d("TAG","back button pressed from: " + Thread.currentThread().name)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Back")
                    builder.setMessage("Want to go back?")

                    //to go back to home menu
                    builder.setPositiveButton("Yes") { dialog, which ->
                        Navigation.findNavController(view).navigate(R.id.action_gameViewFragment_to_homeFragment)
                    }

                    //not useful to handle no button
                    builder.setNegativeButton("No") { dialog, which ->}
                    builder.show()
                } else {
                    // If you want to get default implementation of onBackPressed, use this
                    this.remove();
                    requireActivity().onBackPressed();
                }
            }
        })
    }
    override fun onPause() {
        Log.d("SingleGameFragment.kt" , "onPause()")
        gameView!!.pause()
        super.onPause()
    }

    override fun onStop() {
        Log.d("SingleGameFragment.kt" , "onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("SingleGameFragment.kt" , "onDestroy()")
        super.onDestroy()
    }

}