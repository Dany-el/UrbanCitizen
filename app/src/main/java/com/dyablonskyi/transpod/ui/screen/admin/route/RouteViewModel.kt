package com.dyablonskyi.transpod.ui.screen.admin.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyablonskyi.transpod.data.di.repository.RouteRepository
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteViewModel @Inject constructor(
    private val repo: RouteRepository,
) : ViewModel() {
    private val _routes = MutableStateFlow<MutableList<Route>>(mutableListOf())
    val routes: StateFlow<List<Route>> get() = _routes.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun loadRoutes() {
        _isLoading.value = true
        viewModelScope.launch {
            _routes.value = repo.getAll().toMutableList()
            _isLoading.value = false
        }
    }

    fun insertRoute(route: Route) {
        viewModelScope.launch {
            repo.insert(route)
            loadRoutes()
        }
    }

    fun getRoutesByName(name: String?): List<Route> {
        var list: List<Route> = emptyList()
        viewModelScope.launch {
            list = repo.getByName(name)
        }
        return list
    }

    fun countDriversPerIndividualRoute(): List<RouteDriverCount> {
        var list: List<RouteDriverCount> = emptyList()
        viewModelScope.launch {
            list = repo.countDriversPerIndividualRoute()
        }
        return list
    }

    fun countTotalDriversPerRoute(): List<RouteDriverCount> {
        var list: List<RouteDriverCount> = emptyList()
        viewModelScope.launch {
            list = repo.countTotalDriversPerRoute()
        }
        return list
    }

    fun getRoutesWithTransportTypeOf(type: TransportType): List<Route> {
        var list: List<Route> = emptyList()
        viewModelScope.launch {
            list = repo.getRoutesWithTransportTypeOf(type)
        }
        return list
    }
}