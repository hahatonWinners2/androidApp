package com.quo.hackaton.domain.usecase

import android.util.Log
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Coordinates
import com.quo.hackaton.domain.repository.ClientsRepository
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetClientsUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val getAddressUseCase: GetAddressUseCase,
) {
    suspend operator fun invoke(): List<Company> = coroutineScope {
        val rawClients = clientsRepository.getClients()

        val deferredClients: List<Deferred<Company>> = rawClients.map { dto ->
            async(Dispatchers.IO) {
                val coords = try {
                    getAddressUseCase(dto.address)
                } catch (e: Exception) {
                    Log.d("Search Result", e.toString())
                    Coordinates(address = dto.address, point = Point(0.0, 0.0), name = null)
                }

                dto.copy(
                    lat = coords.point.latitude,
                    lon = coords.point.longitude
                )
            }
        }

        deferredClients.awaitAll()
    }
}