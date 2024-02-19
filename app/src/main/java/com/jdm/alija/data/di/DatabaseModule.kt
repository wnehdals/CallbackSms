package com.jdm.alija.data.di

import android.content.Context
import androidx.room.Room
import com.jdm.alija.data.AppDatabase
import com.jdm.alija.data.dao.SmsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "AlijaApp.db")
            .allowMainThreadQueries()
            .build()
    }
    @Provides
    @Singleton
    fun provideAlarmDao(appDatabase: AppDatabase): SmsDao {
        return appDatabase.smsDao()
    }
}