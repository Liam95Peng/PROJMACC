package com.example.projmacc.Game.gameobject

import android.graphics.Canvas
import com.example.projmacc.Game.GameDisplay

/**
 * GameObject is an abstract class which is the foundation of all world objects in the game.
 */
abstract class GameObject {
    var positionX = 0.0
        protected set
    var positionY = 0.0
        protected set
    @JvmField
    var velocityX = 0.0
    @JvmField
    var velocityY = 0.0
    var directionX = 1.0
        protected set
    var directionY = 0.0
        protected set

    constructor() {}
    constructor(positionX: Double, positionY: Double) {
        this.positionX = positionX
        this.positionY = positionY
    }

    abstract fun draw(canvas: Canvas?, gameDisplay: GameDisplay?)
    abstract fun update()

    fun getX() : Double{
        return this.positionX
    }

    fun getY() : Double{
        return this.positionY
    }

    fun getPlayerVelocityX() : Double{
        return  this.velocityX
    }

    fun getPlayerVelocityY() : Double{
        return this.velocityY
    }

    companion object {
        /**
         * getDistanceBetweenObjects returns the distance between two game objects
         * @param obj1
         * @param obj2
         * @return
         */
        fun getDistanceBetweenObjects(obj1: GameObject, obj2: GameObject): Double {
            return Math.sqrt(
                Math.pow(obj2.positionX - obj1.positionX, 2.0) +
                        Math.pow(obj2.positionY - obj1.positionY, 2.0)
            )
        }
    }
}