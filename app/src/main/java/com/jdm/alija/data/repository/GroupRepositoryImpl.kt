package com.jdm.alija.data.repository

import com.jdm.alija.data.dao.GroupDao
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.data.entity.toGroup
import com.jdm.alija.data.entity.toGroupEntity
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao
): GroupRepository {
    override suspend fun insert(name: String): Long {
        return groupDao.insert(GroupEntity(name))
    }

    override suspend fun getAllGroup(): List<Group> {
        return groupDao.selectAll().map { it.toGroup() }
    }

    override suspend fun delete(group: Group): Int {
        return groupDao.delete(group.toGroupEntity())
    }

    override suspend fun update(group: Group): Int {
        return groupDao.update(group.toGroupEntity())
    }
}