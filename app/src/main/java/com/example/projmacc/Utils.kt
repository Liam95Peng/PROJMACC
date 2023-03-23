package com.example.projmacc

object Utils {
    /**
     * getDistanceBetweenPoints returns the distance between 2d points p1 and p2
     * @param p1x
     * @param p1y
     * @param p2x
     * @param p2y
     * @return
     */
    fun getDistanceBetweenPoints(p1x: Double, p1y: Double, p2x: Double, p2y: Double): Double {
        return Math.sqrt(Math.pow(p1x - p2x, 2.0) + Math.pow(p1y - p2y, 2.0))
    }
}