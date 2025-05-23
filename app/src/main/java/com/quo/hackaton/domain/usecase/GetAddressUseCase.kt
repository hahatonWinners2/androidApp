package com.quo.hackaton.domain.usecase

import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(
    private val repo: AddressRepository
) {
    suspend operator fun invoke(): List<Address> = repo.getAddresses()
}