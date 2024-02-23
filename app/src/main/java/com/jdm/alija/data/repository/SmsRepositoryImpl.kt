package com.jdm.alija.data.repository

import android.util.Log
import com.jdm.alarmlocation.data.entity.SmsEntity
import com.jdm.alija.data.dao.SmsDao
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.SmsRepository
import javax.inject.Inject

class SmsRepositoryImpl @Inject constructor(
    private val smsDao: SmsDao
) : SmsRepository {
    override suspend fun insert(id: String, imgUri: String, text: String, mobile: String, isKakao: Boolean, isHidden: Boolean ): Long {
        return smsDao.insert(SmsEntity(id, imgUri, text, mobile, isKakao, isHidden))
    }

    override suspend fun getAllSms(): List<SmsEntity> {
        return  smsDao.selectAll()
    }

    override suspend fun deleteSms(smsEntity: SmsEntity): Int {
        return smsDao.delete(smsEntity)
    }

    override suspend fun update(smsEntity: SmsEntity): Int {
        Log.e("repository", smsEntity.toString())
        return smsDao.update(smsEntity)
    }
}