package com.example.projmacc.Game.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.projmacc.R

class SpriteSheet(context: Context) {
    private val bitmap : Bitmap
    val playerSpriteArray: ArrayList<Sprite> = ArrayList(2)
    val spellSpriteArray: ArrayList<Sprite> = ArrayList(2)
    val enemySpriteArray: ArrayList<Sprite> = ArrayList(1)
    val grassSprite : Sprite
    val stoneSprite : Sprite

    fun getSpriteByIndex(idxRow : Int , idxCol : Int) : Sprite {
        return Sprite(this ,
            Rect(idxCol * SPRITE_WIDTH_PIXELS,
                idxRow * SPRITE_HEIGHT_PIXELS,
                (idxCol + 1) * SPRITE_WIDTH_PIXELS,
                (idxRow + 1 ) * SPRITE_HEIGHT_PIXELS))

    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    companion object {
        private const val SPRITE_WIDTH_PIXELS = 64
        private const val SPRITE_HEIGHT_PIXELS = 64
    }

    init{
        var bitmapOptions : BitmapFactory.Options = BitmapFactory.Options()
        bitmapOptions.inScaled = false
        this.bitmap = BitmapFactory.decodeResource(context.resources , R.drawable.sprite_sheet_game , bitmapOptions)

        playerSpriteArray.add(getSpriteByIndex(0 , 3))
        playerSpriteArray.add(getSpriteByIndex(0 , 4))
        spellSpriteArray.add(getSpriteByIndex(0 , 5))
        enemySpriteArray.add(getSpriteByIndex(2 , 0))
        grassSprite = getSpriteByIndex(1,1)
        stoneSprite = getSpriteByIndex(1 , 0)
    }
}