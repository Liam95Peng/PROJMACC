package com.example.projmacc.Game.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.projmacc.Game.graphics.SpriteSheet

internal abstract class Tile(protected val mapLocationRect: Rect?) {
    enum class TileType {
        GRASS_TILE,
        STONE_TILE
    }

    abstract fun draw(canvas: Canvas?)

    companion object {
        fun getTile(idxTileType: Int, spriteSheet: SpriteSheet, mapLocationRect: Rect?): Tile? {
            return when (TileType.values()[idxTileType]) {
                TileType.GRASS_TILE -> GrassTile(spriteSheet, mapLocationRect)
                TileType.STONE_TILE -> StoneTile(spriteSheet, mapLocationRect)
            }
        }
    }
}