package com.example.projmacc.Game.gameobject

import android.content.Context
import android.graphics.Paint


/**
 * Circle is an abstract class which implements a draw method from GameObject for drawing the object
 * as a circle.
 */
abstract class Circle(
    context: Context?,
    color: Int,
    positionX: Double,
    positionY: Double,
    radius: Double
) : GameObject(positionX, positionY) {
    protected var paint: Paint
    protected val radius: Double

    companion object {
        /**
         * isColliding checks if two circle objects are colliding, based on their positions and radii.
         * @param obj1
         * @param obj2
         * @return
         */
        fun isColliding(obj1: Circle, obj2: Circle): Boolean {
            val distance = getDistanceBetweenObjects(obj1, obj2)
            val distanceToCollision = obj1.radius + obj2.radius
            return if (distance < distanceToCollision) true else false
        }
    }

    init {
        this.radius = radius
        // Set colors of circle
        paint = Paint()
        paint.color = color
    }
}