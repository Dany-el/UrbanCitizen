package com.dyablonskyi.transpod.data.local.db.entity

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity("route")
data class Route(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val routeStart: String,
    val routeEnd: String,
)

data class RouteDriverCount(
    @ColumnInfo(name = "id") val routeId: Int,
    @ColumnInfo(name = "name") val routeName: String,
    @ColumnInfo(name = "driver_count") val driverCount: Int
)
