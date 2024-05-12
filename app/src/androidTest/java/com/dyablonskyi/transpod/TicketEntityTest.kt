package com.dyablonskyi.transpod

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dyablonskyi.transpod.data.local.db.AppDatabase
import com.dyablonskyi.transpod.data.local.db.entity.Duration
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.TicketBuilder
import com.dyablonskyi.transpod.data.local.db.dao.TicketDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TicketEntityTest {
    private lateinit var ticketDao: TicketDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        ticketDao = db.getTicketDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndReadTicket() {
        val ticket1 = TicketBuilder(Duration.MONTH, null).build()
        val ticket2 = TicketBuilder(Duration.HOUR, null).build()
        val ticket3 = TicketBuilder(Duration.HOUR, null).build()
        val ticket4 = TicketBuilder(Duration.YEAR, null).build()

        val tickets: List<Ticket>
        runBlocking {
            ticketDao.insert(ticket1)
            ticketDao.insert(ticket2)
            ticketDao.insert(ticket3)
            ticketDao.insert(ticket4)
            tickets = ticketDao.getAll()
        }

        val matchResultList = listOf(ticket2, ticket3, ticket1, ticket4)

        assertThat(tickets, equalTo(matchResultList))
    }
}