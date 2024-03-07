package com.jdm.alija.domain.repository

import com.jdm.alija.domain.model.Group

interface GroupRepository {
    suspend fun insert(group: Group): Long
    suspend fun getAllGroup(): List<Group>
    suspend fun delete(group: Group): Int
    suspend fun update(group: Group): Int
    suspend fun getGroup(groupName: String): Group
    suspend fun getGroup(id: Int): Group
}