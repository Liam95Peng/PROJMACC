package com.example.projmacc.Game

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import androidx.navigation.Navigation
import com.example.projmacc.R
import java.lang.IllegalArgumentException

class ThreadGame(gameView: GameView, surfaceHolder: SurfaceHolder) : Thread() {


    private var isRunning = false
    private val updateRate: Double = 1.0/30.0
    //private var nextStatTime: Long = 0L
    var FPS  = 0.0
    var UPS = 0.0


    private val surfaceHolder = surfaceHolder
    private val gameView = gameView


    fun startThreadGame(){
        Log.d("Tag" , "THREAD init")

        isRunning = true
        start()
        Log.d("TAG" , "thread started")
    }
    override fun run() {
        super.run()
        Log.d("TAG" , "running thread")
        var canvas: Canvas ?= Canvas()
        /*
        var accumulator = 0.0
        var currentTime: Long
        var lastUpdate : Long = System.currentTimeMillis()
        nextStatTime = System.currentTimeMillis() + 1000L
        */

        var updateCount = 0
        var frameCount = 0
        var startTime: Long
        var elapsedTime: Long
        var sleepTime: Long

        startTime = System.currentTimeMillis()

        while (isRunning){
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    gameView.draw(canvas)
                    updateCount++
                    update()
                }
            } catch (e : IllegalArgumentException){
                e.printStackTrace()
            }finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                        frameCount++
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                //accumulator -= updateRate
            }
            elapsedTime = System.currentTimeMillis() - startTime
            sleepTime =((updateCount * UPS_PERIOD - elapsedTime).toLong())
            if(sleepTime > 0){
                try{
                    sleep(sleepTime)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            while(sleepTime < 0 && updateCount < MAX_UPS -1){
                //arenaView.update()
                updateCount++
                elapsedTime = System.currentTimeMillis() - startTime
                sleepTime = ((updateCount*UPS_PERIOD - elapsedTime).toLong())
            }
            elapsedTime = System.currentTimeMillis() - startTime
            if(elapsedTime >= 1000){
                UPS = updateCount / (1E-3 * elapsedTime)
                FPS = frameCount / (1E-3 * elapsedTime)
                updateCount = 0
                frameCount = 0
                startTime = System.currentTimeMillis()
            }
        }
    }

    private fun update() {

        gameView.update()
        UPS++
    }


    fun stopLoop() {
        Log.d("GameLoop.java", "stopLoop()")
        isRunning = false
        try {
            join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    companion object  {
        const val MAX_UPS = 30.0
        private const val UPS_PERIOD: Double = 1E+3/ MAX_UPS
    }
}

