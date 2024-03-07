package com.jdm.alija.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdm.alija.data.entity.ContactEntity

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarmEntity: ContactEntity): Long

    @Delete
    suspend fun delete(alarmEntity: ContactEntity): Int


    @Query("SELECT * FROM CONTACT")
    suspend fun selectAll(): List<ContactEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alarmEntity: ContactEntity): Int

    @Query("SELECT * FROM CONTACT WHERE groupName = :groupName")
    suspend fun selectContactByGroupName(groupName: String): List<ContactEntity>

    @Query("DELETE FROM CONTACT WHERE groupName = :groupName")
    suspend fun deleteContactByGroupName(groupName: String)

    @Query("SELECT * FROM CONTACT WHERE mobile = :mobile")
    suspend fun selectContactByMobile(mobile: String): ContactEntity?
}