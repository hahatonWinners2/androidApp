package com.quo.hackaton.domain.repository

import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status

interface AddressRepository {
    suspend fun getAddresses(): List<Company>
    suspend fun updateStatus(id: String, status: Status)
}