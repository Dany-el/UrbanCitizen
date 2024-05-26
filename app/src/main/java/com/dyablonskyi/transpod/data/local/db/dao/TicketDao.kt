package com.dyablonskyi.transpod.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import java.time.LocalDateTime

@Dao
interface TicketDao {

    @Insert
    suspend fun insert(ticket: Ticket)

    @Query("SELECT * FROM ticket ORDER BY CAST((julianday(endDate) - julianday(startDate)) AS INTEGER) ASC")
    suspend fun getAll(): List<Ticket>

    @Query("SELECT * FROM ticket WHERE startDate BETWEEN :from AND :to")
    suspend fun getTicketFromDates(from: LocalDateTime, to: LocalDateTime): List<Ticket>
}