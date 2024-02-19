package com.jdm.alija.domain.repository

import com.jdm.alarmlocation.data.entity.SmsEntity
import com.jdm.alija.domain.model.Contact

interface SmsRepository {
    suspend fun insert(id: String, imgUri: String, text: String, mobile: String): Long
    suspend fun getAllSms(): List<SmsEntity>
    suspend fun deleteSms(smsEntity: SmsEntity): Int
    suspend fun update(smsEntity: SmsEntity): Int
}