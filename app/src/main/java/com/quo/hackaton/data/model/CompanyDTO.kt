package com.quo.hackaton.data.model

import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import java.util.UUID

data class CompanyDTO(
    val id: String,
    val address: String,
    val buildingType: String?,
    val suspicion: Int,
    val lat: Double?,
    val lng: Double?,
)

fun CompanyDTO.toDomain(): Company = Company(
    id      = UUID.fromString(id),
    name    = buildingType ?: "ООО «Тмыв Бабла»",
    address = address,
    lat     = null,
    lon     = null,
    status  = if (suspicion > 0) Status.VIOLATION else Status.PENDING  // TODO
)