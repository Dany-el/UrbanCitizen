package com.dyablonskyi.transpod.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.DriverRepository
import com.dyablonskyi.transpod.data.di.repository.RouteRepository
import com.dyablonskyi.transpod.data.di.repository.TicketRepository
import com.dyablonskyi.transpod.data.di.repository.TransportRepository
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val transportRepository: TransportRepository,
    private val routeRepository: RouteRepository,
    private val ticketRepository: TicketRepository,
) : ViewModel() {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> get() = _tickets.asStateFlow()

    private val _drivers = MutableStateFlow<List<DriverWithRouteAndTransport>>(emptyList())
    val drivers: StateFlow<List<DriverWithRouteAndTransport>> get() = _drivers.asStateFlow()

    private val _transports = MutableStateFlow<List<Transport>>(emptyList())
    val transports: StateFlow<List<Transport>> get() = _transports.asStateFlow()

    private val _availableTransports = MutableStateFlow<List<Transport>>(emptyList())
    val availableTransports: StateFlow<List<Transport>> get() = _availableTransports.asStateFlow()

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> get() = _routes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadTickets() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _tickets.value = ticketRepository.getAll()
            }
            _isLoading.value = false
        }
    }

    fun insertTicket(ticket: Ticket) {
        viewModelScope.launch {
            ticketRepository.insert(ticket)
            loadTickets()
        }
    }

    fun loadDrivers() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _drivers.value = driverRepository.getDriversWithRouteAndTransport()
            }
            _isLoading.value = false
        }
    }

    fun insertDriver(driver: Driver) {
        viewModelScope.launch {
            driverRepository.insert(driver)
            loadDrivers()
        }
    }

    fun loadTransports() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _transports.value = transportRepository.getAll()
            }
            _isLoading.value = false
        }
    }

    fun loadAvailableTransports() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _availableTransports.value = transportRepository.getAllAvailable()
            }
            _isLoading.value = false
        }
    }

    fun insertTransport(transport: Transport) {
        viewModelScope.launch {
            transportRepository.insert(transport)
            loadTransports()
        }
    }

    fun loadRoutes() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _routes.value = routeRepository.getAll()
            }
            _isLoading.value = false
        }
    }

    fun insertRoute(route: Route) {
        viewModelScope.launch {
            routeRepository.insert(route)
            loadRoutes()
        }
    }

    fun loadAll(){
        viewModelScope.launch {
            loadTransports()
        }
        viewModelScope.launch {
            loadDrivers()
        }
        viewModelScope.launch {
            loadRoutes()
        }
    }
}