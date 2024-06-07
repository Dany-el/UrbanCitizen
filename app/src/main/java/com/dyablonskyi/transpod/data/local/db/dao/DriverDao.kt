package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport

@Dao
interface DriverDao {

    @Insert
    suspend fun insert(driver: Driver)

    @Query("SELECT * FROM driver ORDER BY fullName DESC")
    suspend fun getAll(): List<Driver>

    @Query("SELECT * FROM driver")
    suspend fun getDriverWithRouteAndTransport(): List<DriverWithRouteAndTransport>

    @Query("SELECT * FROM driver WHERE routeId = :routeId")
    suspend fun getDriverByRouteId(routeId: Long): List<Driver>

    @Query(
        """
        SELECT COUNT(*)
        FROM driver
        WHERE transportId IS NULL
        """
    )
    suspend fun countDriverWithoutTransport(): Int

    @Query(
        """
        SELECT * FROM driver
        WHERE id NOT IN (SELECT id FROM driver WHERE routeID IS NOT NULL)
        """
    )
    suspend fun getDriverWithoutRoute(): List<Driver>
}
