package com.jdm.alija.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdm.alija.data.dao.SmsDao
import com.jdm.alarmlocation.data.entity.SmsEntity

@Database(
    entities = [SmsEntity::class], version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun smsDao(): SmsDao
}