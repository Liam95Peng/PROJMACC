package com.example.projmacc.Game.gamepanel

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.projmacc.Utils


class Joystick(centerPositionX : Double , centerPositionY: Double,
               outerRadius: Double , innerRadius: Double) {
    private var outerCircleCenterPositionX : Double
    private var outerCircleCenterPositionY : Double
    private var innerCircleCenterPositionX : Double
    private var innerCircleCenterPositionY : Double
    private val outerCircleRadius : Double
    private val innerCircleRadius : Double
    private val outerCirclePaint : Paint = Paint()
    private val innerCirclePaint : Paint = Paint()
    private var isPressed : Boolean
    private var actuatorX : Double
    private var actuatorY : Double

    fun draw(canvas: Canvas) {
        canvas.drawCircle(outerCircleCenterPositionX.toFloat() ,
            outerCircleCenterPositionY.toFloat() , outerCircleRadius.toFloat() , outerCirclePaint)
        canvas.drawCircle(innerCircleCenterPositionX.toFloat() ,
            innerCircleCenterPositionY.toFloat() , innerCircleRadius.toFloat() , innerCirclePaint)
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition(){
        innerCircleCenterPositionX = outerCircleCenterPositionX + actuatorX * outerCircleRadius
        innerCircleCenterPositionY = outerCircleCenterPositionY + actuatorY * outerCircleRadius
    }

    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
        val joystickCenterToTouchDistance = Utils.getDistanceBetweenPoints(
            outerCircleCenterPositionX , outerCircleCenterPositionY , touchPositionX , touchPositionY)

        return joystickCenterToTouchDistance <= this.outerCircleRadius
    }

    fun setIsPressed(isPressed: Boolean) {
        this.isPressed = isPressed
    }

    fun getIsPressed(): Boolean {
        return this.isPressed
    }

    fun setActuator(moveToPositionX: Double, moveToPositionY: Double) {
        val deltaX = moveToPositionX - this.outerCircleCenterPositionX
        val deltaY = moveToPositionY - this.outerCircleCenterPositionY
        val deltaDistance = Utils.getDistanceBetweenPoints(deltaX , deltaY , 0.0 , 0.0)

        if(deltaDistance <= this.outerCircleRadius){
            actuatorX = deltaX/this.outerCircleRadius
            actuatorY = deltaY/this.outerCircleRadius
        }
        else{
            actuatorX = deltaX/deltaDistance
            actuatorY = deltaY/deltaDistance
        }
    }

    fun resetActuator() {
        actuatorX = 0.0
        actuatorY = 0.0
    }

    fun getActuatorX(): Double {
        return this.actuatorX
    }

    fun getActuatorY(): Double {
        return this.actuatorY
    }

    init {
        this.outerCircleCenterPositionX = centerPositionX
        this.outerCircleCenterPositionY = centerPositionY

        this.innerCircleCenterPositionX = centerPositionX
        this.innerCircleCenterPositionY = centerPositionY

        this.outerCircleRadius = outerRadius
        this.innerCircleRadius = innerRadius

        this.outerCirclePaint.color = Color.WHITE
        this.outerCirclePaint.style = Paint.Style.FILL_AND_STROKE

        this.innerCirclePaint.color = Color.RED
        this.innerCirclePaint.style = Paint.Style.FILL_AND_STROKE

        this.isPressed = false

        this.actuatorX = 0.0
        this.actuatorY = 0.0
    }
}
