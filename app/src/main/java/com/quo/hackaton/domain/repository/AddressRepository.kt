package com.quo.hackaton.domain.repository

import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.model.Status

interface AddressRepository {
    suspend fun getAddresses(): List<Address>
    suspend fun updateStatus(id: String, status: Status)
}