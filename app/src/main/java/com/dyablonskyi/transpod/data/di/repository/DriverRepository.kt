package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.dao.DriverDao
import javax.inject.Inject

class DriverRepository @Inject constructor(
    private val driverDao: DriverDao
) {
    suspend fun insert(driver: Driver) = driverDao.insert(driver)

    suspend fun getAll() = driverDao.getAll()

    suspend fun getDriversWithRouteAndTransport() = driverDao.getDriverWithRouteAndTransport()

    suspend fun getDriversByRouteId(routeId: Long) = driverDao.getDriverByhRouteId(routeId)

    suspend fun countDriversWithoutTransport() = driverDao.countDriverWithoutTransport()

    suspend fun getDriversWithoutRoute() = driverDao.getDriverWithoutRoute()
}