package com.dyablonskyi.transpod

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dyablonskyi.transpod.data.local.db.AppDatabase
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.data.local.db.dao.DriverDao
import com.dyablonskyi.transpod.data.local.db.dao.TransportDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TransportEntityTest {
    private lateinit var driverDao: DriverDao
    private lateinit var transportDao: TransportDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        transportDao = db.getTransportDao()
        driverDao = db.getDriverDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndReadTransport() {
        val transport = TestUtil.createTransport()

        val transports: List<Transport>
        runBlocking {
            transportDao.insert(transport)
            transports = transportDao.getAll()
        }

        assertThat(transports[0], equalTo(transport))
    }

    @Test
    @Throws(IOException::class)
    fun readAvailableTransport(){
        val transport1 = TestUtil.createTransport(number = 1233).copy(number = 1)
        val transport2 = TestUtil.createTransport(number = 1235).copy(number = 2)
        val transport3 = TestUtil.createTransport(number = 1237).copy(number = 3)

        val availableTransports: List<Transport>
        runBlocking {
            transportDao.insert(transport1)
            transportDao.insert(transport2)
            transportDao.insert(transport3)

            val driver = TestUtil.createDriver(transportId = transport2.number.toLong())

            driverDao.insert(driver)
            availableTransports = transportDao.getAllAvailable()
        }

        val matcherList = listOf(transport1, transport3)

        assertThat(availableTransports, equalTo(matcherList))
    }
}