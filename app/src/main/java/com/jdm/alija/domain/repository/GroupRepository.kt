package com.jdm.alija.domain.repository

import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.domain.model.Group

interface GroupRepository {
    suspend fun insert(name: String): Long
    suspend fun getAllGroup(): List<Group>
    suspend fun delete(group: Group): Int
    suspend fun update(group: Group): Int
}