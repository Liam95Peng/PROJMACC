package com.example.projmacc.Game.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

import androidx.core.content.ContextCompat
import com.example.projmacc.R
import com.example.projmacc.Game.GameDisplay
import com.example.projmacc.Game.gameobject.Player


/**
 * HealthBar display the players health to the screen
 */
class HealthBar(context: Context?, player: Player) {
    private val player: Player = player
    private val borderPaint: Paint = Paint()
    private val healthPaint: Paint = Paint()
    private val width = 100F
    private val height = 20F
    private val margin = 2F

    fun draw(canvas: Canvas?, gameDisplay: GameDisplay?) {
        val x: Float = player.getX().toFloat()
        val y: Float = player.getY().toFloat()
        val distanceToPlayer = 30F
        val healthPointPercentage = player.getHealthPoints().toFloat() / Player.MAX_HEALTH_POINTS

        // Draw border
        val borderLeft: Float = x - width / 2
        val borderRight: Float = x + width / 2
        val borderBottom: Float = y - distanceToPlayer
        val borderTop: Float = borderBottom - height

        canvas?.drawRect(
            gameDisplay!!.gameToDisplayCoordinatesX(borderLeft.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesY(borderTop.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesX(borderRight.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesY(borderBottom.toDouble()).toFloat(),
            borderPaint
        )

        // Draw health
        val healthWidth : Float = width - 2*margin
        val healthHeight : Float = height - 2*margin
        val healthLeft : Float = borderLeft + margin
        var healthRight : Float = healthLeft + healthWidth * healthPointPercentage
        val healthBottom : Float = borderBottom - margin
        val healthTop : Float = healthBottom - healthHeight

        val drawRect = canvas?.drawRect(
            gameDisplay!!.gameToDisplayCoordinatesX(healthLeft.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesY(healthTop.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesX(healthRight.toDouble()).toFloat(),
            gameDisplay!!.gameToDisplayCoordinatesY(healthBottom.toDouble()).toFloat(),
            healthPaint
        )
    }

    init {

        val borderColor = ContextCompat.getColor(context!!, R.color.healthBarBorder)
        borderPaint.color = borderColor

        val healthColor = ContextCompat.getColor(context, R.color.healthBarHealth)
        healthPaint.color = healthColor
    }
}