package com.quo.hackaton.domain.usecase

import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.repository.ClientsRepository
import java.util.UUID
import javax.inject.Inject

class UpdateClientsStatusUseCase @Inject constructor(
    private val repo: ClientsRepository
) {
    suspend operator fun invoke(id: UUID, status: Status) = repo.updateStatus(id, status)
}
