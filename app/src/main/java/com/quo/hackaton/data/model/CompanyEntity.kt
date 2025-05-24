package com.quo.hackaton.data.model

data class CompanyEntity(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val status: String
)