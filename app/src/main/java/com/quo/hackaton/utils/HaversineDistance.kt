package com.quo.hackaton.utils

import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.geometry.Point
import kotlin.math.*

fun haversineDistance(a: Point, b: Point): Double {
    val R = 6371.0
    val dLat = Math.toRadians(b.latitude - a.latitude)
    val dLon = Math.toRadians(b.longitude - a.longitude)
    val lat1 = Math.toRadians(a.latitude)
    val lat2 = Math.toRadians(b.latitude)

    val sinDlat = sin(dLat / 2).pow(2.0)
    val sinDlon = sin(dLon / 2).pow(2.0)
    val h = sinDlat + sinDlon * cos(lat1) * cos(lat2)
    val c = 2 * atan2(sqrt(h), sqrt(1 - h))
    return R * c
}

fun MutableList<RequestPoint>.sortedByDistanceTo(userLocation: Point): MutableList<RequestPoint> {
    return this.sortedBy { haversineDistance(it.point, userLocation) }.toMutableList()
}