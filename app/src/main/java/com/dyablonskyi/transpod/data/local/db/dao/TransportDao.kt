package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dyablonskyi.transpod.data.local.db.entity.Transport

@Dao
interface TransportDao {

    @Insert
    suspend fun insert(transport: Transport)

    @Query("SELECT * FROM transport ORDER BY type")
    suspend fun getAll(): List<Transport>

    @Query("SELECT * FROM transport WHERE number NOT IN (SELECT transportId FROM driver WHERE transportId IS NOT NULL)")
    suspend fun getAllAvailable(): List<Transport>
}