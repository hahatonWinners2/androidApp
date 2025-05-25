package com.quo.hackaton.domain.usecase

import com.quo.hackaton.domain.model.Coordinates
import com.quo.hackaton.domain.repository.YandexRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repo: YandexRepository
) {
    suspend operator fun invoke(name: String): Coordinates = repo.getCoordinatesByText(name)
}