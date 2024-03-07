package com.jdm.alija.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.data.entity.GroupEntity

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GroupEntity): Long

    @Delete
    suspend fun delete(entity: GroupEntity): Int


    @Query("SELECT * FROM `Group`")
    suspend fun selectAll(): List<GroupEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: GroupEntity): Int

    @Query("SELECT * FROM `Group` WHERE name = :groupName")
    suspend fun selectGroupByGroupName(groupName: String): GroupEntity
    @Query("SELECT * FROM `Group` WHERE id = :id")
    suspend fun selectGroupById(id: Int): GroupEntity

}