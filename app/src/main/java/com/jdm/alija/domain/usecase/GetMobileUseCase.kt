package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.toContract
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.MobileRepository
import kotlinx.coroutines.async
import javax.inject.Inject

class GetMobileUseCase @Inject constructor(
    private val mobileRepository: MobileRepository
) {
    suspend operator fun invoke(): List<Contact>{
        val contacts =  mobileRepository.getPhoneContacts()
        val contactNumber = mobileRepository.getContactNumbers()
        contacts.forEach {
            contactNumber[it.id]?.let { numbers ->
                it.numbers = numbers
            }
        }
        return contacts.map { it.toContract() }
    }
}
