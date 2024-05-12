package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport

@Dao
interface DriverDao {

    @Insert
    suspend fun insert(driver: Driver)

    @Query("SELECT * FROM driver ORDER BY fullName DESC")
    suspend fun getAll(): List<Driver>

    @Transaction
    @Query("SELECT * FROM driver")
    suspend fun getDriverWithRouteAndTransport(): List<DriverWithRouteAndTransport>
}
