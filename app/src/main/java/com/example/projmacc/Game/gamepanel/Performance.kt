package com.example.projmacc.Game.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.projmacc.R
import com.example.projmacc.Game.ThreadGame
import java.util.*


class Performance(context: Context, gameLoop: ThreadGame) {
    private var gameLoop : ThreadGame = gameLoop
    private val context : Context = context

    fun draw(canvas: Canvas?){
        //drawUPS(canvas)
        drawFPS(canvas!!)
    }

    /*fun drawUPS(canvas: Canvas) {
        val averageUPS = "%.2f".format(Locale.ENGLISH,gameLoop.UPS)
        val paint = Paint()
        val color =  ContextCompat.getColor(context , R.color.magenta)
        paint.color = color
        paint.textSize = 50F
        canvas.drawText("UPS: $averageUPS", 100F , 50F , paint)
    }*/

    private fun drawFPS(canvas: Canvas){
        val averageFPS = "%.2f".format(Locale.ENGLISH,gameLoop.FPS)
        val paint = Paint()
        val color =  ContextCompat.getColor(context , R.color.magenta)
        paint.color = color
        paint.textSize = 50F
        canvas.drawText("FPS: $averageFPS", 100F, 50F , paint)
    }
}