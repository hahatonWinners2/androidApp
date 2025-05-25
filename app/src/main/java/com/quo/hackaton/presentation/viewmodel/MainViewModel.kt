package com.quo.hackaton.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.model.Status
import com.quo.hackaton.domain.usecase.GetClientsUseCase
import com.quo.hackaton.domain.usecase.UpdateClientsStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAddresses: GetClientsUseCase,
    private val updateStatus: UpdateClientsStatusUseCase
) : ViewModel() {
    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies.asStateFlow()

    init {
        viewModelScope.launch {
            _companies.value = getAddresses()
        }
    }

    fun onStatusChanged(company: Company, newStatus: Status) {
        viewModelScope.launch {
            updateStatus(company.id, newStatus)
            _companies.update { list ->
                list.map { if (it.id == company.id) it.copy(status = newStatus) else it }
            }
        }
    }
}