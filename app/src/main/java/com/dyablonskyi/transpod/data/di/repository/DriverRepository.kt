package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.dao.DriverDao
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DriverRepository @Inject constructor(
    private val driverDao: DriverDao
) {

    suspend fun insert(driver: Driver) = withContext(Dispatchers.IO) {
        driverDao.insert(driver)
    }

    suspend fun getAll(): List<Driver> = withContext(Dispatchers.IO) {
        return@withContext driverDao.getAll()
    }

    suspend fun getDriversWithRouteAndTransport(): List<DriverWithRouteAndTransport> =
        withContext(Dispatchers.IO) {
            return@withContext driverDao.getDriverWithRouteAndTransport()
        }

    suspend fun getDriversByRouteId(routeId: Long) = withContext(Dispatchers.IO) {
        return@withContext driverDao.getDriverByRouteId(routeId)
    }

    suspend fun countDriversWithoutTransport() = withContext(Dispatchers.IO) {
        return@withContext driverDao.countDriverWithoutTransport()
    }

    suspend fun getDriversWithoutRoute() = withContext(Dispatchers.IO) {
        return@withContext driverDao.getDriverWithoutRoute()
    }
}