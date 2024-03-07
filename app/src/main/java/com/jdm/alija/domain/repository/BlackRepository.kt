package com.jdm.alija.domain.repository

import com.jdm.alija.data.entity.BlackEntity
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact

interface BlackRepository {
    suspend fun insert(blackEntity: BlackContact): Long
    suspend fun getAllContact(): List<BlackContact>
    suspend fun deleteSms(blackEntity: BlackContact): Int
    suspend fun update(blackEntity: BlackContact): Int
}