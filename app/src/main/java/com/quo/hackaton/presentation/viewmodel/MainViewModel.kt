package com.quo.hackaton.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quo.hackaton.domain.model.Company
import com.quo.hackaton.domain.usecase.GetClientsUseCase
import com.quo.hackaton.domain.usecase.UpdateCommentUseCase
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
    private val updateComment: UpdateCommentUseCase
) : ViewModel() {
    private val _companies = MutableStateFlow<List<Company>>(emptyList())
    val companies: StateFlow<List<Company>> = _companies.asStateFlow()

    init {
        viewModelScope.launch {
            _companies.value = getAddresses()
        }
    }

    fun onCommentUpdate(company: Company, comment: String) {
        viewModelScope.launch {
            updateComment(company.id, comment)
            _companies.update { list ->
                list.map { if (it.id == company.id) it.copy(comment = comment) else it }
            }
        }
    }
}