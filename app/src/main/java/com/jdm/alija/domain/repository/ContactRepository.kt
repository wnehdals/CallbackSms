package com.jdm.alija.domain.repository

import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group

interface ContactRepository {
    suspend fun insert(contact: ContactEntity): Long
    suspend fun getAllContact(): List<ContactEntity>
    suspend fun deleteSms(smsEntity: ContactEntity): Int
    suspend fun update(smsEntity: ContactEntity): Int
    suspend fun deleteContactByGroupName(group: Group)
    suspend fun selectContactByMobile(mobile: String): ContactEntity?
}