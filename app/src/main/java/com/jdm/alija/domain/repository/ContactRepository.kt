package com.jdm.alija.domain.repository

import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.domain.model.Group

interface ContactRepository {
    suspend fun insert(id: String, imgUri: String, text: String, mobile: String, isKakao: Boolean, isHidden: Boolean, groupName: String, isIncall: Boolean,
                       isOutCall: Boolean,
                       isReleaseCall: Boolean ): Long
    suspend fun getAllContact(): List<ContactEntity>
    suspend fun deleteSms(smsEntity: ContactEntity): Int
    suspend fun update(smsEntity: ContactEntity): Int
    suspend fun deleteContactByGroupName(group: Group)
}