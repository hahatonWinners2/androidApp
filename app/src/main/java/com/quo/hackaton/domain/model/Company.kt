package com.quo.hackaton.domain.model

import java.util.UUID

data class Company(
    val id: UUID,
    val name: String = "ООО \"Тмыв Бабла\"",
    val address: String,
    val lat: Double?,
    val lon: Double?,
    var status: Status = Status.PENDING
)

enum class Status { PENDING, OK, VIOLATION }