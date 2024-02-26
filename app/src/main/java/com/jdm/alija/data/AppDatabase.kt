package com.jdm.alija.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdm.alija.data.dao.ContactDao
import com.jdm.alija.data.dao.GroupDao
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.data.entity.TypeResponseConverter

@Database(
    entities = [GroupEntity::class, ContactEntity::class], version = 1
)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun contactDao(): ContactDao
}