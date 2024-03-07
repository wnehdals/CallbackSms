package com.jdm.alija.data.di

import android.content.Context
import androidx.room.Room
import com.jdm.alija.data.AppDatabase
import com.jdm.alija.data.dao.BlackListDao
import com.jdm.alija.data.dao.ContactDao
import com.jdm.alija.data.dao.GroupDao
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
        return AppDatabase.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase): ContactDao {
        return appDatabase.contactDao()
    }
    @Provides
    @Singleton
    fun provideGroupDao(appDatabase: AppDatabase): GroupDao {
        return appDatabase.groupDao()
    }
    @Provides
    @Singleton
    fun blackListDao(appDatabase: AppDatabase): BlackListDao {
        return appDatabase.blackListDao()
    }
}