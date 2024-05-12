package com.dyablonskyi.transpod.data.di.modules

import android.content.Context
import androidx.room.Room
import com.dyablonskyi.transpod.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, AppDatabase::class.java, "app_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesTicketDao(database: AppDatabase) =
        database.getTicketDao()

    @Provides
    fun providesRouteDao(database: AppDatabase) =
        database.getRouteDao()

    @Provides
    fun providesDriverDao(database: AppDatabase) =
        database.getDriverDao()

    @Provides
    fun providesTransportDao(database: AppDatabase) =
        database.getTransportDao()
}