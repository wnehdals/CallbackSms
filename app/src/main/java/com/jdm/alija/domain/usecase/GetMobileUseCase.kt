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
        val newContact = mutableListOf<Contact>()
        for(contact in contacts) {
            var mobile = ""
            if (contact.numbers.size > 1) {
                for(number in contact.numbers) {
                    if (number.length > 4 && number.substring(0,3) == "010") {
                        mobile = number
                        break
                    }
                }
            } else if (contact.numbers.size == 1){
                if (contact.numbers[0].length > 4 && contact.numbers[0].substring(0,3) == "010") {
                    mobile = contact.numbers[0]
                }
            }
            if (mobile.isNotEmpty()) {
                newContact.add(contact.toContract(mobile))
            }
        }
        return newContact
    }
}
