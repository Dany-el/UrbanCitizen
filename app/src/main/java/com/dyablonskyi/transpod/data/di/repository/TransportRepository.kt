package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.data.local.db.dao.TransportDao
import javax.inject.Inject

class TransportRepository @Inject constructor(
    private val transportDao: TransportDao
) {
    suspend fun insert(transport: Transport) = transportDao.insert(transport)

    suspend fun getAll() = transportDao.getAll()

    suspend fun getAllAvailable() = transportDao.getAllAvailable()
}