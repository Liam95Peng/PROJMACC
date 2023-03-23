package com.example.projmacc.Game.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.example.projmacc.Game.GameDisplay
import com.example.projmacc.Game.graphics.SpriteSheet


class Tilemap(spriteSheet: SpriteSheet) {
    private val mapLayout : MapLayout
    private lateinit var tilemap : Array<Array<Tile?>>
    private val spriteSheet: SpriteSheet
    private var mapBitmap: Bitmap? = null

    private fun initializeTilemap(){
        val layout = mapLayout.getLayout()
        tilemap = Array(MapLayout.NUMBER_OF_ROW_TILES) { arrayOfNulls(MapLayout.NUMBER_OF_COLUMN_TILES)}
        for (iRow in 0 until MapLayout.NUMBER_OF_ROW_TILES){
            for (jCol in 0 until  MapLayout.NUMBER_OF_COLUMN_TILES){
                tilemap[iRow][jCol] = Tile.getTile(layout!![iRow]!![jCol],
                    spriteSheet,
                    getRectByIndex(iRow, jCol))
            }
        }
        val config = Bitmap.Config.ARGB_8888
        mapBitmap = Bitmap.createBitmap(
            MapLayout.NUMBER_OF_COLUMN_TILES * MapLayout.TILE_WIDTH_PIXELS,
            MapLayout.NUMBER_OF_ROW_TILES * MapLayout.TILE_HEIGHT_PIXELS,
            config
        )
        val mapCanvas = Canvas(mapBitmap!!)
        for (iRow in 0 until MapLayout.NUMBER_OF_ROW_TILES) {
            for (iCol in 0 until MapLayout.NUMBER_OF_COLUMN_TILES) {
                tilemap[iRow][iCol]!!.draw(mapCanvas)
            }
        }
    }

    private fun getRectByIndex(idxRow: Int, idxCol: Int): Rect {
        return Rect(
            idxCol * MapLayout.TILE_WIDTH_PIXELS,
            idxRow * MapLayout.TILE_HEIGHT_PIXELS,
            (idxCol + 1) * MapLayout.TILE_WIDTH_PIXELS,
            (idxRow + 1) * MapLayout.TILE_HEIGHT_PIXELS
        )
    }

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawBitmap(
            mapBitmap!!,
            gameDisplay.gameRect,
            gameDisplay.DISPLAY_RECT,
            null
        )
    }

    init {
        this.mapLayout = MapLayout()
        this.spriteSheet = spriteSheet
        initializeTilemap()
    }
}