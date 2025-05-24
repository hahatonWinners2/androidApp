package com.quo.hackaton.data.repository

import com.quo.hackaton.data.api.AddressApi
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.repository.AddressRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AddressRepositoryImpl@Inject constructor(
    private val api: AddressApi
) : AddressRepository{
    private val mockList = listOf(
        Company("1", "ООО \"ОЦРВ\"", "г. Сириус, ул. Светлая, 12, кв. 45", 43.403501, 39.961537),
        Company("2", "ООО \"ЦОС\"", "г. Сириус, пр. Ленина, 45к2",     43.409913, 39.968832),
        Company("3", "ООО \"Сёрф\"","г. Сириус, ул. Молодежная, 52", 43.395536, 39.973117),
        Company("4", "ООО \"Сбер\"", "г. Сириус, ул. Светлая, 12, кв. 45", 43.580425, 39.721139),
//        Company("5", "ООО \"ВК\"", "г. Сириус, пр. Ленина, 45к2",     54.771244, 36.628423),
//        Company("6", "ООО \"Т-Банк\"","г. Сириус, ул. Молодежная, 52", 55.661244, 36.708423),
//        Company("7", "ООО \"Яндекс\"", "г. Сириус, ул. Светлая, 12, кв. 45", 55.851844, 37.488423),
//        Company("8", "ООО \"МЦСТ\"", "г. Сириус, пр. Ленина, 45к2",     53.771244, 37.628423),
//        Company("9", "ООО \"Т1\"","г. Сириус, ул. Молодежная, 52", 56.766644, 37.666666),
//        Company("10", "ООО \"Сириус\"", "г. Сириус, ул. Светлая, 12, кв. 45", 56.7619244, 39.918423),
//        Company("11", "ООО \"Альфа\"", "г. Сириус, пр. Ленина, 45к2",     58.778244, 37.888423),
//        Company("12", "ООО \"Райффайзен\"","г. Сириус, ул. Молодежная, 52", 54.961944, 37.908423),
    )

    override suspend fun getAddresses(): List<Company> {
        delay(500) // имитация сети
        return mockList
    }

    override suspend fun updateStatus(id: String, status: Status)  {
        delay(200)
        println("Отправлено: $id -> $status")
    }
}