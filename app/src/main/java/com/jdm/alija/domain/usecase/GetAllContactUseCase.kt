package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.toContract
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.MobileRepository
import javax.inject.Inject

class GetAllContactUseCase  @Inject constructor(
    private val mobileRepository: MobileRepository,
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(): List<Contact>{
        val mobiles =  mobileRepository.getPhoneContacts()
        val contactNumber = mobileRepository.getContactNumbers()
        val contacts = contactRepository.getAllContact()
        mobiles.forEach {
            contactNumber[it.id]?.let { numbers ->
                it.numbers = numbers
            }
        }
        val tempContacts = mobiles.map { it.toContract() }

        for(tempContact in tempContacts) {
            for(contact in contacts) {
                if (tempContact.id == contact.id) {
                    tempContact.isSelected = true
                    tempContact.text = contact.text
                    tempContact.imgUri = contact.imgUri
                    tempContact.isKakao = contact.isKakao
                    tempContact.isCheck = contact.isCheck
                    tempContact.mobile = contact.mobile
                    tempContact.groupName = contact.groupName
                    tempContact.isIncall = contact.isIncall
                    tempContact.isOutCall = contact.isOutCall
                    tempContact.isReleaseCall = contact.isReleaseCall
                    tempContact.isEnable = if (contact.groupName.isEmpty()) true else false
                    tempContact.isSelected = if (contact.groupName.isEmpty()) false else true
                    break
                }
            }
        }
        return tempContacts
    }
}
