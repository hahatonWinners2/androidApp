package com.quo.hackaton.domain.model

import com.yandex.mapkit.geometry.Point

data class Coordinates(
    val address: String?,
    val name: String?,
    val point: Point
)
