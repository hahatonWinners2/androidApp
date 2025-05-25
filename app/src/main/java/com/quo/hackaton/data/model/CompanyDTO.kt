package com.quo.hackaton.data.model

import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import java.util.UUID

data class CompanyDTO(
    val id: String,
    val address: String,
    val buildingType: String?,
    val suspicion: Int,
)

fun CompanyDTO.toDomain(): Company = Company(
    id      = UUID.fromString(id),
    name    = buildingType ?: "ООО «Тмыв Бабла»",
    address = address,
    lat     = 0.0,
    lon     = 0.0,
    status  = Status.PENDING
)