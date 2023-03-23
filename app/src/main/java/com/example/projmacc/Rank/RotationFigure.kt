package com.example.projmacc.Rank

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.WindowManager



class RotationFigure(context: Context) : SensorEventListener {
    private val context : Context = context
    private val sensorManager : SensorManager
    private val rotation : Sensor

    private var rotationVector = FloatArray(3)
    private var rotationMatrix = FloatArray(16)
    private var rotationMatrixAdjusted = FloatArray(16)
    fun getRotationMatrix() : FloatArray{
        return this.rotationMatrix
    }

    fun register(){
        sensorManager.registerListener(this , rotation , SensorManager.SENSOR_DELAY_GAME)
    }

    fun pause(){
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager?
        val mDisplay = wm!!.defaultDisplay


        if (event.sensor?.type== Sensor.TYPE_ROTATION_VECTOR){
            rotationVector = event.values.clone()
        }

        SensorManager.getRotationMatrixFromVector(rotationMatrix,rotationVector)

        //rotationMatrix = rotationMatrixAdjusted.clone()



    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }
}