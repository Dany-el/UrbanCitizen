package com.dyablonskyi.transpod.ui.screen.admin.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.RouteRepository
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val repo: RouteRepository,
) : ViewModel() {
    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> get() = _routes.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadRoutes() {
        _isLoading.value = true
        viewModelScope.launch {
            _routes.value = repo.getAll()
            _isLoading.value = false
        }
    }

    fun insertRoute(route: Route) {
        viewModelScope.launch {
            repo.insert(route)
            loadRoutes()
        }
    }

    suspend fun filterRoutesByName(name: String?) {
        _isLoading.value = true
        viewModelScope.launch {
            _routes.value = repo.getByName(name)
            _isLoading.value = false
        }
    }

    suspend fun countDriversPerIndividualRoute(): List<RouteDriverCount> {
        val list = viewModelScope.async {
            repo.countDriversPerIndividualRoute()
        }
        return list.await()
    }

    suspend fun countTotalDriversPerRoute(): List<RouteDriverCount> {
        val list = viewModelScope.async {
            repo.countTotalDriversPerRoute()
        }
        return list.await()
    }

    suspend fun filterRoutesWithTransportTypeOf(type: TransportType) {
        _isLoading.value = true
        viewModelScope.launch {
            _routes.value = repo.getRoutesWithTransportTypeOf(type)
            _isLoading.value = false
        }
    }
}