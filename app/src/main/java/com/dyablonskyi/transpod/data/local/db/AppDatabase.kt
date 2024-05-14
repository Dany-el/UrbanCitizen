package com.dyablonskyi.transpod.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dyablonskyi.transpod.data.local.db.dao.DriverDao
import com.dyablonskyi.transpod.data.local.db.dao.RouteDao
import com.dyablonskyi.transpod.data.local.db.dao.TicketDao
import com.dyablonskyi.transpod.data.local.db.dao.TransportDao
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.Transport

@Database(
    entities = [
        Driver::class,
        Transport::class,
        Route::class,
        Ticket::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDriverDao(): DriverDao
    abstract fun getTransportDao(): TransportDao
    abstract fun getRouteDao(): RouteDao
    abstract fun getTicketDao(): TicketDao
}