package com.quo.hackaton.domain.repository

import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import java.util.UUID

interface ClientsRepository {
    suspend fun getAddresses(checked: Boolean = false): List<Company>
    suspend fun updateStatus(id: UUID, status: Status)
}