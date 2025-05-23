package com.quo.hackaton.domain.usecase

import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.repository.AddressRepository
import javax.inject.Inject

class UpdateAddressStatusUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(id: String, status: Status) = repo.updateStatus(id, status)
}
