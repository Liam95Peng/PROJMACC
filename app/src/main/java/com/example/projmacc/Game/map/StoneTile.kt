package com.example.projmacc.Game.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.projmacc.Game.graphics.Sprite
import com.example.projmacc.Game.graphics.SpriteSheet


internal class StoneTile(spriteSheet: SpriteSheet, mapLocationRect: Rect?) : Tile(mapLocationRect) {
    private val sprite: Sprite
    override fun draw(canvas: Canvas?) {
        sprite.draw(canvas!!, mapLocationRect!!.left, mapLocationRect.top)
    }

    init {
        sprite = spriteSheet.stoneSprite
    }
}
