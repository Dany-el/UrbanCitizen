package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dyablonskyi.transpod.data.local.db.entity.Route

@Dao
interface RouteDao {

    @Insert
    suspend fun insert(route: Route)

    @Query("SELECT * FROM route ORDER BY name ASC")
    suspend fun getAll(): List<Route>
}