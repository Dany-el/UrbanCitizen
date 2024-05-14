package com.dyablonskyi.transpod.data.local.db.entity

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(
    "driver"
)
data class Driver(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val phoneNumber: String,
    val address: String,
    val routeId: Long?,
    val transportId: Long?,
)

@Immutable
data class DriverWithRouteAndTransport(
    @Embedded val driver: Driver,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "id"
    )
    val route: Route?,
    @Relation(
        parentColumn = "transportId",
        entityColumn = "number"
    )
    val transport: Transport?
)