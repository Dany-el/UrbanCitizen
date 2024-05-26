package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.data.local.db.entity.TransportType

@Dao
interface RouteDao {

    @Insert
    suspend fun insert(route: Route)

    @Query("SELECT * FROM route ORDER BY name ASC")
    suspend fun getAll(): List<Route>

    @Query("SELECT * FROM route WHERE name LIKE '%' || :search || '%'")
    suspend fun getByName(search: String?): List<Route>

    @Transaction
    @Query(
        """
        SELECT r.id, r.name, COUNT(d.id) AS driver_count
        FROM route r
        JOIN driver d ON r.id = d.routeId
        GROUP BY r.id, r.name
        """
    )
    suspend fun countTotalDriversPerRoute(): List<RouteDriverCount>

    @Transaction
    @Query(
        """
        SELECT id, name, (
	    SELECT COUNT(*)
	    FROM driver d
	    WHERE d.routeId = r.id
        ) AS driver_count
        FROM route r
        """
    )
    suspend fun countDriversPerIndividualRoute(): List<RouteDriverCount>

    @Transaction
    @Query(
        """
        SELECT *
        FROM route
        WHERE id IN (SELECT routeId
                        FROM driver
                        JOIN transport ON driver.transportId = transport.number
                        WHERE transport.type = :type)
        """
    )
    suspend fun getRouteWithTransportTypeOf(type: TransportType): List<Route>
}