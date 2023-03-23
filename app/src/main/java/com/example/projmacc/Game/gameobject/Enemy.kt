package com.example.projmacc.Game.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat

import com.example.projmacc.R

import com.example.projmacc.Game.GameDisplay
import com.example.projmacc.Game.ThreadGame
import com.example.projmacc.Game.graphics.Sprite

/*
    Enemy is a character which always moves in the direction of the player
    Enemy class is an extension of a Circle which is an extension of a ArenaObject
 */
class Enemy
    : Circle {

    private val player : Player
    private val enemySpriteArray: ArrayList<Sprite>


    constructor(context: Context , player: Player ,
                enemySpriteArray: ArrayList<Sprite>) : super(
        context,
        ContextCompat.getColor(context , R.color.enemy),
        Math.random()*1000,
        Math.random()*1000,
        30.0
    ){

        this.player = player
        this.enemySpriteArray = enemySpriteArray
    }

    private val SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6
    private val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / ThreadGame.MAX_UPS

    override fun draw(canvas: Canvas?, arenaDisplay: GameDisplay?) {
        enemySpriteArray[0].draw(canvas!!,
            arenaDisplay!!.gameToDisplayCoordinatesX(positionX).toInt() - enemySpriteArray[0].getWidth() / 2,
            arenaDisplay!!.gameToDisplayCoordinatesY(positionY).toInt() - enemySpriteArray[0].getHeight() / 2
        )
    }

    override fun update() {
        //update velocity of the enemy in order to be in the direction of the player

        //calculate vector from enemy to player (in x and y), target - initial
        var distanceToPlayerX : Double = player.getX() - positionX
        var distanceToPlayerY : Double = player.getY() - positionY

        //calculate (absolute) distance between enemy and player
        var distanceToPlayer : Double = getDistanceBetweenObjects(this , player)

        //calculate direction from enemy to player, normalized
        var directionX : Double = distanceToPlayerX/distanceToPlayer
        var directionY : Double = distanceToPlayerY/distanceToPlayer

        //set velocity in the direction of the player
        if(distanceToPlayer > 0){
            velocityX = directionX * MAX_SPEED
            velocityY = directionY * MAX_SPEED
        }
        else{
            velocityX = 0.0
            velocityY = 0.0
        }

        //update the position of the enemy
        positionX += velocityX
        positionY += velocityY
    }

    companion object{

        private val SPAWN_PER_MINUTE = 60.0
        private val SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0
        private val UPDATES_PER_SPAWN = ThreadGame.MAX_UPS/SPAWN_PER_SECOND
        private var updatesUntilNextSpawn = UPDATES_PER_SPAWN

        //check if a new enemy should spawn, spawn enemy per minute
        fun readyToSpawn() : Boolean {
            if (updatesUntilNextSpawn <= 0){
                updatesUntilNextSpawn += UPDATES_PER_SPAWN
                return true
            } else {
                updatesUntilNextSpawn --
                return false
            }
        }
    }


}
