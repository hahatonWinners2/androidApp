package com.quo.hackaton.data.repository

import com.quo.hackaton.data.api.AddressApi
import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.repository.AddressRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AddressRepositoryImpl@Inject constructor(
    private val api: AddressApi
) : AddressRepository{
    private val mockList = listOf(
        Address("1", "Ул. Ленина, д.10", 55.751244, 37.618423),
        Address("2", "Пр. Мира, д.3",     55.771244, 37.628423),
        Address("3", "Пл. Революции, д.1", 55.761244, 37.608423)
    )

    override suspend fun getAddresses(): List<Address> {
        delay(500) // имитация сети
        return mockList
    }

    override suspend fun updateStatus(id: String, status: Status)  {
        delay(200)
        println("Отправлено: $id -> $status")
    }
}