package com.jdm.alija.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdm.alija.data.entity.BlackEntity
import com.jdm.alija.data.entity.ContactEntity
@Dao

interface BlackListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blackEntity: BlackEntity): Long

    @Delete
    suspend fun delete(blackEntity: BlackEntity): Int


    @Query("SELECT * FROM Black")
    suspend fun selectAll(): List<BlackEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(blackEntity: BlackEntity): Int

}