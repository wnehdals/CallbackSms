package com.jdm.alija.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jdm.alija.data.dao.BlackListDao
import com.jdm.alija.data.dao.ContactDao
import com.jdm.alija.data.dao.GroupDao
import com.jdm.alija.data.entity.BlackEntity
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.data.entity.TypeResponseConverter
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(
    entities = [GroupEntity::class, ContactEntity::class, BlackEntity::class], version = 1
)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun contactDao(): ContactDao
    abstract fun blackListDao(): BlackListDao
    companion object {
        fun getInstance(context: Context): AppDatabase = Room
            .databaseBuilder(context, AppDatabase::class.java, "AlijaApp.db")
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        runBlocking {
                            getInstance(context).groupDao().insert(GroupEntity.getDefaultGroup())
                        }
                    }
                }
            })
            .allowMainThreadQueries()
            .build()
    }
}