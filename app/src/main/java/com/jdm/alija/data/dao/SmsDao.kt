package com.jdm.alija.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdm.alarmlocation.data.entity.SmsEntity

@Dao
interface SmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmEntity: SmsEntity): Long

    @Delete
    suspend fun delete(alarmEntity: SmsEntity): Int


    @Query("SELECT * FROM CALLBACKSMS")
    suspend fun selectAll(): List<SmsEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alarmEntity: SmsEntity): Int
}