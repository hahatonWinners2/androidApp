package com.quo.hackaton.domain.model

data class Address(
    val id: String,  // TODO STRING OR INT OR LONG OR UUID
    val title: String,
    val lat: Double,
    val lon: Double,
    var status: Status = Status.PENDING
)

enum class Status { PENDING, OK, VIOLATION }