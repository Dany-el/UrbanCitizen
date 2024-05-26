package com.dyablonskyi.transpod.ui.screen.admin.transport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.TransportRepository
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val repo: TransportRepository
) : ViewModel() {
    private val _transports = MutableStateFlow<List<Transport>>(emptyList())
    val transports: StateFlow<List<Transport>> get() = _transports.asStateFlow()

    private val _availableTransports = MutableStateFlow<List<Transport>>(emptyList())
    val availableTransports: StateFlow<List<Transport>> get() = _availableTransports.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadTransports() {
        _isLoading.value = true
        viewModelScope.launch {
            _transports.value = repo.getAll()
            _isLoading.value = false
        }
    }

    fun insertTransport(transport: Transport) {
        viewModelScope.launch {
            repo.insert(transport)
            loadTransports()
        }
    }

    fun getAllAvailableTransports(): List<Transport> {
        var list: List<Transport> = emptyList()
        viewModelScope.launch {
            list = repo.getAllAvailable()
        }
        return list
    }
}