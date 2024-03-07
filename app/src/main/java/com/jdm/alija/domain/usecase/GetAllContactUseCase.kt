package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.toContract
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.repository.MobileRepository
import javax.inject.Inject

class GetAllContactUseCase @Inject constructor(
    private val getMobileUseCase: GetMobileUseCase,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(): List<Contact> {
        val tempContacts = getMobileUseCase.invoke()
        val groups = groupRepository.getAllGroup()

        for (tempContact in tempContacts) {
            for (group in groups) {
                for (contact in group.contactList) {
                    if (tempContact.id == contact.id) {
                        tempContact.isSelected = if (contact.groupName.isEmpty()) false else true
                        tempContact.isEnable = if (contact.groupName.isEmpty()) true else false
                        tempContact.mobile = contact.mobile
                        tempContact.groupName = contact.groupName
                        break
                    }
                }
            }
        }
        return tempContacts
    }
}
