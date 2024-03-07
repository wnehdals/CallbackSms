package com.jdm.alija.data.repository

import com.jdm.alija.data.dao.BlackListDao
import com.jdm.alija.data.entity.BlackEntity
import com.jdm.alija.data.entity.toBlackContact
import com.jdm.alija.data.entity.toBlackEntity
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.repository.BlackRepository
import javax.inject.Inject

class BlackRepositoryImpl @Inject constructor(
    private val blackListDao: BlackListDao
) : BlackRepository{
    override suspend fun insert(blackEntity: BlackContact): Long {
        return blackListDao.insert(blackEntity.toBlackEntity(true))
    }

    override suspend fun getAllContact(): List<BlackContact> {
        return blackListDao.selectAll().map { it.toBlackContact() }
    }

    override suspend fun deleteSms(blackEntity: BlackContact): Int {
        return blackListDao.delete(blackEntity.toBlackEntity())
    }

    override suspend fun update(blackEntity: BlackContact): Int {
        return blackListDao.update(blackEntity.toBlackEntity())
    }
}