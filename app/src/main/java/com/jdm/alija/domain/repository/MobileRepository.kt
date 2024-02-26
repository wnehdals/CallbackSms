package com.jdm.alija.domain.repository

import com.jdm.alija.data.entity.MobileEntity
import com.jdm.alija.domain.model.Contact


interface MobileRepository {
    suspend fun getPhoneContacts(): ArrayList<MobileEntity>
    suspend fun getContactNumbers(): HashMap<String, ArrayList<String>>
}