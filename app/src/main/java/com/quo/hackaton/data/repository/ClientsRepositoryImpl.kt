package com.quo.hackaton.data.repository

import android.util.Log
import com.quo.hackaton.data.api.ClientsApi
import com.quo.hackaton.data.model.toDomain
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.repository.ClientsRepository
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

class ClientsRepositoryImpl@Inject constructor(
    private val api: ClientsApi
) : ClientsRepository{
//    private val mockList = listOf(
//        Company("1", "Частный", "Краснодарский край, г Краснодар, улица Митрофана Седина, д. 191", 45.040419, 38.982302),
//        Company("2", "Частный", "Краснодарский край, г Краснодар, улица Циолковского, д. 122",     45.088815, 38.979185),
//        Company("3", "Частный","Краснодарский край, посёлок Южный улица Мира, д. 70,", 45.150114, 39.020058),
//        Company("4", "Частный", "Краснодарский край, г Краснодар, Плановый проезд, д. 5", 45.034133, 39.016689),
//        Company("5", "Частный", "Краснодарский край, г Краснодар, Восточно-Кругликовская улица, 48/2", 45.060191, 39.027963),
//        Company("6", "Прочий", "Краснодарский край, г Краснодар, Черниговская улица, д 1", 45.039731, 39.018226),
//        Company("7", "Частный", "Республика Адыгея, р-н Тахтамукайский, аул Козет, ул Шоссейная, д. 44", 44.990542, 38.998400),
//        Company("5", "ООО \"ВК\"", "г. Сириус, пр. Ленина, 45к2",     54.771244, 36.628423),
//        Company("6", "ООО \"Т-Банк\"","г. Сириус, ул. Молодежная, 52", 55.661244, 36.708423),
//        Company("7", "ООО \"Яндекс\"", "г. Сириус, ул. Светлая, 12, кв. 45", 55.851844, 37.488423),
//        Company("8", "ООО \"МЦСТ\"", "г. Сириус, пр. Ленина, 45к2",     53.771244, 37.628423),
//        Company("9", "ООО \"Т1\"","г. Сириус, ул. Молодежная, 52", 56.766644, 37.666666),
//        Company("10", "ООО \"Сириус\"", "г. Сириус, ул. Светлая, 12, кв. 45", 56.7619244, 39.918423),
//        Company("11", "ООО \"Альфа\"", "г. Сириус, пр. Ленина, 45к2",     58.778244, 37.888423),
//        Company("12", "ООО \"Райффайзен\"","г. Сириус, ул. Молодежная, 52", 54.961944, 37.908423),
//    )

    override suspend fun getClients(checked: Boolean): List<Company> {
        return api.getClients().map { dto -> dto.toDomain()}
    }

    override suspend fun updateStatus(id: UUID, status: Status)  {
        delay(200)
        Log.d("Status", "Отправлено: $id -> $status")  // TODO
    }
}