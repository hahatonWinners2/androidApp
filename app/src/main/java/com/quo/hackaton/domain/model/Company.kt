package com.quo.hackaton.domain.model

data class Company(
    val id: String,  // TODO STRING OR INT OR LONG OR UUID
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    var status: Status = Status.PENDING
)

enum class Status { PENDING, OK, VIOLATION }