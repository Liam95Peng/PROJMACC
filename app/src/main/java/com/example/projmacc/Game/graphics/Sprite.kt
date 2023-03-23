package com.example.projmacc.Game.graphics

import android.graphics.Canvas
import android.graphics.Rect

class Sprite(spriteSheet: SpriteSheet, rect: Rect) {

    private val spriteSheet : SpriteSheet
    private val rect : Rect

    fun draw(canvas: Canvas, x: Int, y: Int){
        canvas.drawBitmap(spriteSheet.getBitmap() ,
            rect ,
            Rect(x ,
                y ,
                x+getWidth() ,
                y+getWidth()) ,
            null)
    }
    fun getWidth(): Int {
        return rect.width()
    }

    fun getHeight(): Int {
        return rect.height()
    }

    init {
        this.spriteSheet = spriteSheet
        this.rect = rect
    }
}