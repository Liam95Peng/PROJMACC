package com.example.projmacc.Game.graphics

import android.graphics.Canvas

import com.example.projmacc.Game.GameDisplay
import com.example.projmacc.Game.gameobject.Player
import com.example.projmacc.Game.gameobject.PlayerState

class Animator(playerSpriteArray: ArrayList<Sprite>) {
    private val idxNotShootingFrame = 0
    private var idxShootingFrame = 1
    private val playerSpriteArray : ArrayList<Sprite> = playerSpriteArray

    fun drawPlayer(canvas: Canvas?, gameDisplay: GameDisplay, player: Player) {
        when(player.playerState.state){
            PlayerState.State.NOT_SHOOTING -> drawPlayerFrame(canvas ,
                gameDisplay,
                player,
                playerSpriteArray[idxNotShootingFrame]
            )
            PlayerState.State.SHOOTING -> drawPlayerFrame(canvas ,
                gameDisplay,
                player,
                playerSpriteArray[idxShootingFrame]
            )
        }
    }


    fun drawPlayerFrame(canvas: Canvas?, gameDisplay: GameDisplay, player: Player, sprite: Sprite?) {
        sprite?.draw(
            canvas!!,
            gameDisplay.gameToDisplayCoordinatesX(player.positionX).toInt() - sprite.getWidth() / 2,
            gameDisplay.gameToDisplayCoordinatesY(player.positionY).toInt() - sprite.getHeight() / 2
        )
    }


}