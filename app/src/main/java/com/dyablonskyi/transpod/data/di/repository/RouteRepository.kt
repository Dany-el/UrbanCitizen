package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.dao.RouteDao
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeDao: RouteDao
) {
    suspend fun insert(route: Route) = withContext(Dispatchers.IO) { routeDao.insert(route) }

    suspend fun getAll() = withContext(Dispatchers.IO) { routeDao.getAll() }

    suspend fun getByName(name: String?) = withContext(Dispatchers.IO) { routeDao.getByName(name) }

    suspend fun countDriversPerIndividualRoute() =
        withContext(Dispatchers.IO) { routeDao.countDriversPerIndividualRoute() }

    suspend fun countTotalDriversPerRoute() =
        withContext(Dispatchers.IO) { routeDao.countTotalDriversPerRoute() }

    suspend fun getRoutesWithTransportTypeOf(type: TransportType) =
        withContext(Dispatchers.IO) { routeDao.getRouteWithTransportTypeOf(type) }
}