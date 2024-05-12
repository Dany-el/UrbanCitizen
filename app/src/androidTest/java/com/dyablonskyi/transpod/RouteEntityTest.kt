package com.dyablonskyi.transpod

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dyablonskyi.transpod.data.local.db.AppDatabase
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.dao.DriverDao
import com.dyablonskyi.transpod.data.local.db.dao.RouteDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RouteEntityTest {
    private lateinit var driverDao: DriverDao
    private lateinit var routeDao: RouteDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        driverDao = db.getDriverDao()
        routeDao = db.getRouteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndReadRoute(){
        val route = TestUtil.createRoute()


        val routes: List<Route>
        runBlocking {
            routeDao.insert(route)
            routes = routeDao.getAll()
        }
        assertThat(routes[0], equalTo(route))
    }
}