package com.example.projmacc.Rank

import android.content.Context
import android.opengl.GLSurfaceView

class MyView(context: Context?, type: String, rotationFigure: RotationFigure) : GLSurfaceView(context) {

    private val renderer: MyRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyRenderer(context , type , rotationFigure)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}