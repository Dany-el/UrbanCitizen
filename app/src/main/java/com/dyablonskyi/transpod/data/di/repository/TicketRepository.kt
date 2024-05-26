package com.dyablonskyi.transpod.data.di.repository

import com.dyablonskyi.transpod.data.local.db.dao.TicketDao
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun insert(ticket: Ticket) = withContext(Dispatchers.IO) { ticketDao.insert(ticket) }

    suspend fun getAll() = withContext(Dispatchers.IO) { ticketDao.getAll() }

    suspend fun getTicketByStartDateIn(from: LocalDateTime, to: LocalDateTime) =
        withContext(Dispatchers.IO) {
            ticketDao.getTicketFromDates(
                from, to
            )
        }
}