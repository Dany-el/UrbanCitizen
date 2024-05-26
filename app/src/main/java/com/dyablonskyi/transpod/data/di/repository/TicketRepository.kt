package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.dao.TicketDao
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import java.time.LocalDateTime
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun insert(ticket: Ticket) = ticketDao.insert(ticket)

    suspend fun getAll() = ticketDao.getAll()

    suspend fun getTicketByStartDateIn(from: LocalDateTime, to: LocalDateTime) =
        ticketDao.getTicketFromDates(
            from, to
        )
}