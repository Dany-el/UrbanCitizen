package com.dyablonskyi.transpod.ui.screen.admin.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.DriverRepository
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val repo: DriverRepository,
) : ViewModel() {
    private val _drivers = MutableStateFlow<List<DriverWithRouteAndTransport>>(emptyList())
    val drivers: StateFlow<List<DriverWithRouteAndTransport>> get() = _drivers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadDrivers() {
        _isLoading.value = true
        viewModelScope.launch {
            _drivers.value = repo.getDriversWithRouteAndTransport()
            _isLoading.value = false
        }
    }

    fun insertDriver(driver: Driver) {
        viewModelScope.launch {
            repo.insert(driver)
            loadDrivers()
        }
    }

    suspend fun getDriversByRouteId(routeId: Long): List<Driver> {
        val list = viewModelScope.async {
            repo.getDriversByRouteId(routeId)
        }
        return list.await()
    }

    suspend fun countDriversWithoutTransport(): Int {
        val count = viewModelScope.async {
            repo.countDriversWithoutTransport()
        }
        return count.await()
    }
}