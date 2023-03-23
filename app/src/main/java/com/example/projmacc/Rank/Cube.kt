package com.example.projmacc.Rank


import android.content.Context
import android.opengl.GLES20
import com.example.projmacc.R
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


class Cube(context: Context? , rotationFigure: RotationFigure) {
    private var context: Context  = context!!

    private var mProgram: Int
    private val myRenderer = MyRenderer(context, "cube", rotationFigure)

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
                "attribute vec2 aTexCoord;"+
                "varying vec2 vTexCoord;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "vTexCoord = aTexCoord;" +
                "}"

    private var vPMatrixHandle: Int = 0

    private val fragmentShaderCode =
        "precision mediump float;" +
                "varying vec2 vTexCoord;" +
                "uniform sampler2D uTexture0 , uTexture1;"+
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = texture2D(uTexture0, vTexCoord);" +
                "}"

    val COORDS_PER_VERTEX = 4
    private val vertices = arrayOf (
        floatArrayOf(-1.0f , -1.0f , 1.0f, 1.0f), //bottom front left
        floatArrayOf(-1.0f , 1.0f , 1.0f, 1.0f), //top front left
        floatArrayOf(1.0f , 1.0f , 1.0f, 1.0f), //top front right
        floatArrayOf(1.0f , -1.0f , 1.0f, 1.0f), //bottom front right
        floatArrayOf(-1.0f , -1.0f , -1.0f, 1.0f), //bottom back left
        floatArrayOf(-1.0f , 1.0f , -1.0f, 1.0f), //top back left
        floatArrayOf(1.0f , 1.0f , -1.0f, 1.0f), //top back right
        floatArrayOf(1.0f , -1.0f , -1.0f, 1.0f) //bottom back right
    )

    private val texCoord = arrayOf(
        floatArrayOf(0f,0f),
        floatArrayOf(0f,1f),
        floatArrayOf(1f,1f),
        floatArrayOf(1f,0f)
    )

    private var pos = FloatArray(96)
    private var tex = FloatArray(48)

    private var vertexBuffer: Buffer
    private var texBuffer : Buffer

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var texHandle : Int = 0

    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private fun cube(){
        val drawOrder = intArrayOf(
            1, 0, 3, 2 ,
            2, 3, 7, 6 ,
            3, 0, 4, 7 ,
            6, 5, 1, 2 ,
            4, 5, 6, 7 ,
            5, 4, 0, 1 )

        var i = 0;
        while(i < drawOrder.size){
            pos[i*4] = vertices[drawOrder[i]][0]
            pos[i*4+1] = vertices[drawOrder[i]][1]
            pos[i*4+2] = vertices[drawOrder[i]][2]
            pos[i*4+3] =vertices[drawOrder[i]][3]

            tex[i*2] = texCoord[i%4][0]
            tex[i*2+1] = texCoord[i%4][1]

            i++
        }

    }

    init{


        val vertexShader: Int = myRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = myRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }

        cube()

        vertexBuffer = ByteBuffer.allocateDirect(pos.size*32).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(pos )
                position(0)
            }
        }

        texBuffer = ByteBuffer.allocateDirect(tex.size*32).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(tex )
                position(0)
            }
        }


    }




    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment

        GLES20.glUseProgram(mProgram)
        // get handle to vertex shader's vPosition member

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, floatArrayOf(1.0f,0.0f,0.0f,1.0f) , 0)
            }
            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")


            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

            texHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoord").also {
                GLES20.glEnableVertexAttribArray(it)

                GLES20.glVertexAttribPointer(
                    it,
                    2,
                    GLES20.GL_FLOAT,
                    false,
                    8,
                    texBuffer
                )

            }

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, R.drawable.fire)

            // Draw the triangle
            val i = (0..5);
            for(x in i) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 4 * x, 4)
            }

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)

        }
    }


}