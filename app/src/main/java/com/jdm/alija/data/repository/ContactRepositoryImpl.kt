package com.jdm.alija.data.repository

import android.util.Log
import com.jdm.alija.data.dao.ContactDao
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactRepository {
    override suspend fun insert(contact: ContactEntity ): Long {
        return contactDao.insert(contact)
    }

    override suspend fun getAllContact(): List<ContactEntity> {
        return  contactDao.selectAll()
    }

    override suspend fun deleteSms(smsEntity: ContactEntity): Int {
        return contactDao.delete(smsEntity)
    }

    override suspend fun update(smsEntity: ContactEntity): Int {
        Log.e("repository", smsEntity.toString())
        return contactDao.update(smsEntity)
    }

    override suspend fun deleteContactByGroupName(group: Group) {
        return contactDao.deleteContactByGroupName(groupName = group.name)
    }

    override suspend fun selectContactByMobile(mobile: String): ContactEntity? {
        return contactDao.selectContactByMobile(mobile)
    }
}