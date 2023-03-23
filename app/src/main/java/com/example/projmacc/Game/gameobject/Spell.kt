package com.example.projmacc.Game.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.projmacc.Game.graphics.Sprite

import com.example.projmacc.R
import com.example.projmacc.Game.GameDisplay


class Spell(context: Context?, player: Player,sprite: ArrayList<Sprite>) : Circle(
    context,
    ContextCompat.getColor(context!!, R.color.spell),
    player.positionX,
    player.positionY, 25.0
) {
    private val player : Player = player
    private val sprite : Sprite = sprite[0] //just first one

    override fun draw(canvas: Canvas?, gameDisplay: GameDisplay?) {
        sprite.draw(canvas!!,
            gameDisplay!!.gameToDisplayCoordinatesX(positionX).toInt() - sprite.getWidth() / 2 ,
            gameDisplay!!.gameToDisplayCoordinatesY(positionY).toInt() - sprite.getHeight() / 2 )

    }

    override fun update() {
        positionX = positionX + velocityX
        positionY = positionY + velocityY
    }

    companion object {
        const val SPEED_PIXELS_PER_SECOND = 800.0
        private const val MAX_SPEED = SPEED_PIXELS_PER_SECOND / 30.0
    }

    init {
        velocityX = player.directionX * MAX_SPEED
        velocityY = player.directionY * MAX_SPEED
    }
}