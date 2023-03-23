package com.example.projmacc.Game

import android.graphics.Rect
import com.example.projmacc.Game.gameobject.GameObject


class GameDisplay( widthPixels : Int,
                    heightPixels : Int,
                    centerObject: GameObject
) {
    val DISPLAY_RECT: Rect
    private var gameToDisplayCoordinateOffsetX : Double
    private var gameToDisplayCoordinateOffsetY : Double
    private var displayCenterX : Double
    private var displayCenterY : Double
    private var gameCenterX : Double
    private var gameCenterY :Double
    private val centerObject : GameObject
    private val widthPixels : Int
    private val heightPixels : Int


    fun gameToDisplayCoordinatesX(positionX: Double): Double {
        return positionX + gameToDisplayCoordinateOffsetX
    }

    fun gameToDisplayCoordinatesY(positionY: Double): Double {
        return positionY + gameToDisplayCoordinateOffsetY
    }

    fun update(){

        gameCenterX = centerObject.getX()
        gameCenterY = centerObject.getY()

        //CHANGES
        if(
            (((gameCenterX+displayCenterX) >= 3840.0) or ((gameCenterX-displayCenterX) <= 0.0))
            and
            (((gameCenterY+displayCenterY) >= 3840.0) or ((gameCenterY-displayCenterY) <= 0.0))
        ){
            gameCenterX = displayCenterX
            gameCenterY = displayCenterY
        }
        else if(((gameCenterX+displayCenterX) >= 3840.0)
            or
            ((gameCenterX-displayCenterX) <= 0.0)){
            gameCenterX = displayCenterX
            gameToDisplayCoordinateOffsetY = displayCenterY - gameCenterY
        }
        else if(((gameCenterY+displayCenterY) >= 3840.0)
            or
            ((gameCenterY-displayCenterY) <= 0.0)){
            gameCenterY = displayCenterY
            gameToDisplayCoordinateOffsetX = displayCenterX - gameCenterX
        }
        else{
            gameToDisplayCoordinateOffsetX = displayCenterX - gameCenterX
            gameToDisplayCoordinateOffsetY = displayCenterY - gameCenterY
        }

    }

    val gameRect: Rect
        get() = Rect((gameCenterX - widthPixels / 2).toInt(),
            (gameCenterY - heightPixels / 2).toInt(),
            (gameCenterX + widthPixels / 2).toInt(),
            (gameCenterY + heightPixels / 2).toInt())

    init{
        DISPLAY_RECT = Rect(0, 0, widthPixels, heightPixels)
        this.centerObject = centerObject
        this.gameToDisplayCoordinateOffsetX = 0.0
        this.gameToDisplayCoordinateOffsetY = 0.0
        this.widthPixels = widthPixels
        this.heightPixels = heightPixels
        this.displayCenterX = widthPixels/2.0
        this.displayCenterY = heightPixels/2.0
        this.gameCenterX = 0.0
        this.gameCenterY = 0.0
    }
}