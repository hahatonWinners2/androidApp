package com.quo.hackaton.domain.repository

import com.quo.hackaton.domain.model.Coordinates

interface YandexRepository {
    suspend fun getCoordinatesByText(name: String) : Coordinates
}