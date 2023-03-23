package com.example.projmacc.Game.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.projmacc.R


class Score(context: Context) {
    private val context : Context = context
    private var score : Int = 0

    fun update(){
        this.score++
    }

    fun drawScore(canvas: Canvas){
        val paint = Paint()
        val color = ContextCompat.getColor(this.context , R.color.magenta)
        paint.textSize = 50F
        paint.color = color
        canvas.drawText("Score: " + this.score.toString() , 100F , 100F,paint)
    }

    fun getScore() : Int{
        return this.score
    }
    fun setScore(score: Int){
        this.score = score
    }

}