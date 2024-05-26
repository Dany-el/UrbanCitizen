package com.dyablonskyi.transpod.ui.screen.admin.driver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.DriverRepository
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import dagger.hilt.android.lifecycle.HiltViewModel
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
        println("Drivers was read (${_drivers.value.size})")
    }

    fun insertDriver(driver: Driver) {
        viewModelScope.launch {
            repo.insert(driver)
            loadDrivers()
        }
    }

    fun getDriversByRouteId(routeId: Long): List<Driver> {
        var list: List<Driver> = emptyList()
        viewModelScope.launch {
            list = repo.getDriversByRouteId(routeId)
        }
        return list
    }

    fun countDriversWithoutTransport(): Int {
        var count: Int = 0
        viewModelScope.launch {
            count = countDriversWithoutTransport()
        }
        return count
    }
}