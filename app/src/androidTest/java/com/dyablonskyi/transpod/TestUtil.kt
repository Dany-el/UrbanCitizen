package com.dyablonskyi.transpod

import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import java.time.LocalDateTime
import java.time.Month

object TestUtil {
    fun createDriver(
        fullName: String = "Mike Vazovskyi",
        phoneNumber: String = "0123456789",
        address: String = "st. Unavailable",
        routeId: Long? = null,
        transportId: Long? = null
    ): Driver = Driver(
        fullName = fullName,
        phoneNumber = phoneNumber,
        address = address,
        routeId = routeId,
        transportId = transportId
    )

    fun createTransport(
        number: Int = 1234,
        type: TransportType = TransportType.BUS
    ): Transport = Transport(
        number = number,
        type = type
    )

    fun createRoute(
        name: String = "Route name",
        routeStart: String = "st. Start",
        routeEnd: String = "st. End"
    ): Route = Route(
        name = name,
        routeStart = routeStart,
        routeEnd = routeEnd
    )

    fun createTicket(
        startDate: LocalDateTime = LocalDateTime.of(2024, Month.APRIL, 10, 12, 30),
        endDate: LocalDateTime = LocalDateTime.of(2024, Month.APRIL, 10, 13, 30),
        price: Double = 10.0,
        transportId: Long? = null
    ): Ticket = Ticket(
        startDate = startDate,
        endDate = endDate,
        price = price,
        transportId = transportId
    )
}