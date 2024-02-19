package com.jdm.alija.domain.repository

import com.jdm.alija.domain.model.Contact


interface ContractRepository {
    suspend fun getPhoneContacts(): ArrayList<Contact>
    suspend fun getContactNumbers(): HashMap<String, ArrayList<String>>
}