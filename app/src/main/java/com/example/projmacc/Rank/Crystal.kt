package com.example.projmacc.Rank

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.example.projmacc.R
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Crystal(context: Context? , rotationFigure: RotationFigure) {
    private var context: Context  = context!!

    private var mProgram: Int
    private val myRenderer = MyRenderer(context, "crystal", rotationFigure)

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "uniform mat4 uRObjMatrix;"+
                "attribute vec4 vPosition;" +
                "attribute vec2 aTexCoord;"+
                "varying vec2 vTexCoord;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * uRObjMatrix * vPosition;" +
                "vTexCoord = aTexCoord;" +
                "}"

    private var vPMatrixHandle: Int = 0
    private var RObjMatrixHandle: Int = 1
    private var RObjMatrix = FloatArray(16)

    private val fragmentShaderCode =
        "precision mediump float;" +
                "varying vec2 vTexCoord;" +
                "uniform sampler2D uTexture0 , uTexture1;"+
                "uniform vec4 vColor;" +
                "void main() {" +
                " gl_FragColor = texture2D(uTexture1, vTexCoord);" +
                "}"

    val COORDS_PER_VERTEX = 4
    private val vertices = arrayOf (
        floatArrayOf(0.0f , 1.0f , 0.0f, 1.0f), //top vertices
        floatArrayOf(0.0f , -1.0f , 0.0f, 1.0f), //bottom vertices
        floatArrayOf(0.0f , 0.0f , 0.5f, 1.0f), //front vertices
        floatArrayOf(0.0f , 0.0f , -0.5f, 1.0f), //back vertices
        floatArrayOf(-0.5f , 0.0f , 0.0f, 1.0f), //left vertices
        floatArrayOf(0.5f , 0.0f , 0.0f, 1.0f) //right vertices
    )

    private val texCoord = arrayOf(
        floatArrayOf(0f,0f),
        floatArrayOf(0f,1f),
        floatArrayOf(1f,1f),
        floatArrayOf(1f,0f)
    )

    private  var pos = FloatArray(96)
    private var tex = FloatArray(48)

    private var vertexBuffer: Buffer
    private var texBuffer : Buffer

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var texHandle : Int = 0

    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private fun crystal(){
        val drawOrder = intArrayOf(
            0, 4, 2,
            0, 3, 4,
            0, 5, 3,
            0, 2, 5,
            4, 1, 2,
            3, 1, 4,
            5, 1, 3,
            2, 1, 5
        )

        var i = 0;
        while(i < drawOrder.size){
            pos[i*4] = vertices[drawOrder[i]][0]
            pos[i*4+1] = vertices[drawOrder[i]][1]
            pos[i*4+2] = vertices[drawOrder[i]][2]
            pos[i*4+3] = vertices[drawOrder[i]][3]

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

        crystal()

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

            Matrix.setRotateM(RObjMatrix, 0, 90f, 1f, 0f, 0f)
            RObjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uRObjMatrix")


            GLES20.glUniformMatrix4fv(RObjMatrixHandle, 1, false, RObjMatrix, 0)

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
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, R.drawable.crystal)

            // Draw the triangle
            val i = (0..7);
            for(x in i) {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 3 * x, 3)
            }

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)

        }
    }

}