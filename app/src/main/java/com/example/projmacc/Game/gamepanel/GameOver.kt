package com.example.projmacc.Game.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.projmacc.R

/**
 * GameOver is a panel which draws the text Game Over to the screen.
 */
class GameOver(context: Context, width: Double, height: Double) {
    private val context : Context =context
    private val width : Float = width.toFloat()
    private val height : Float = height.toFloat()

    fun draw(canvas: Canvas, s: String, score: Int) {

        val paint = Paint()
        val color: Int = ContextCompat.getColor(context, R.color.lose)
        paint.color = color
        paint.textSize = 150F
        canvas.drawText(s , width-150*3 , height-150 , paint)
        canvas.drawText("Your score is: $score", width-75*8 , height , paint  )
        paint.textSize = 100F
        canvas.drawText("Click back button go to Home" ,
            width-100*8,
            height + 150,
            paint
        )

    }
}