package com.example.proof_of_concept

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
class CompassSensor(context: Context, private val onOrientationChanged: (Float) -> Unit) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    fun start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }
    fun stop() {
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> System.arraycopy(it.values, 0, gravity, 0, it.values.size)
                Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(it.values, 0, geomagnetic, 0, it.values.size)
            }
            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                onOrientationChanged(azimuth)
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}