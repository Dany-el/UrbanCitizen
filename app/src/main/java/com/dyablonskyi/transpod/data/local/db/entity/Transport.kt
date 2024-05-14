package com.dyablonskyi.transpod.data.local.db.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity("transport")
data class Transport(
    @PrimaryKey(autoGenerate = true) val number: Int = 0,
    val type: TransportType
)

