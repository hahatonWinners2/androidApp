package com.quo.hackaton.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.usecase.GetAddressesUseCase
import com.quo.hackaton.domain.usecase.UpdateAddressStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAddresses: GetAddressesUseCase,
    private val updateStatus: UpdateAddressStatusUseCase
) : ViewModel() {
    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    init {
        viewModelScope.launch {
            _addresses.value = getAddresses()
        }
    }

    fun onStatusChanged(address: Address, newStatus: Status) {
        viewModelScope.launch {
            updateStatus(address.id, newStatus)
            _addresses.update { list ->
                list.map { if (it.id == address.id) it.copy(status = newStatus) else it }
            }
        }
    }
}