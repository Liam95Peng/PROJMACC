package com.example.projmacc.Game.gamepanel

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.projmacc.Game.Utils

class Buttons(
    circleCenterPositionX: Double,
    circleCenterPositionY: Double,
    radius: Double
) {
    private var circleCenterPositionX: Double = circleCenterPositionX
    private var circleCenterPositionY: Double = circleCenterPositionY
    private val circleRadius: Double = radius
    private val circlePaint: Paint = Paint()
    private var isPressed = false
    var actuatorX = 0.0
        private set
    var actuatorY = 0.0
        private set

    fun draw(canvas: Canvas) {
        // Draw outer circle
        canvas.drawCircle(
            circleCenterPositionX.toFloat(),
            circleCenterPositionY.toFloat(),
            circleRadius.toFloat(),
            circlePaint
        )
    }


    fun setIsPressed(isPressed: Boolean){
        this.isPressed = isPressed
    }

    fun getIsPressed():Boolean{
        return this.isPressed
    }

    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
        val joystickCenterToTouchDistance = Utils.getDistanceBetweenPoints(
            circleCenterPositionX , circleCenterPositionY,touchPositionX,touchPositionY)

        return joystickCenterToTouchDistance <= this.circleRadius
    }

    init {
        // paint of circles

        this.circlePaint.color = Color.GREEN
        this.circlePaint.style = Paint.Style.FILL_AND_STROKE

    }
}