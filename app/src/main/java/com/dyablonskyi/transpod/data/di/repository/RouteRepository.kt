package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.dao.RouteDao
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeDao: RouteDao
) {
    suspend fun insert(route: Route) = routeDao.insert(route)

    suspend fun getAll() = routeDao.getAll()
}