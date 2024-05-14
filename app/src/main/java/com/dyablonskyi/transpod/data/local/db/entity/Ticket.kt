package com.dyablonskyi.transpod.data.local.db.entity

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Immutable
@Entity(
    "ticket",
    foreignKeys = [
        ForeignKey(
            entity = Transport::class,
            parentColumns = arrayOf("number"),
            childColumns = arrayOf("transportId")
        )
    ]
)
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val price: Double,
    val transportId: Long?,
)

enum class Duration(val price: Double) {
    HOUR(10.0),
    DAY(100.0),
    MONTH(350.0),
    HALF_YEAR(700.0),
    YEAR(960.0)
}

class TicketBuilder(
    private val duration: Duration,
    private val transportId: Long?
) {
    fun build(): Ticket {
        val startDate = LocalDateTime.now()
        val endDate = when (duration) {
            Duration.HOUR -> startDate.plusHours(1)
            Duration.DAY -> startDate.plusDays(1)
            Duration.MONTH -> startDate.plusMonths(1)
            Duration.HALF_YEAR -> startDate.plusMonths(6)
            Duration.YEAR -> startDate.plusYears(1)
        }
        return Ticket(
            startDate = startDate,
            endDate = endDate,
            price = duration.price,
            transportId = transportId
        )
    }
}
