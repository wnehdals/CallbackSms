package com.jdm.alija.domain.usecase

import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.repository.MobileRepository
import javax.inject.Inject

class GetAllGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val mobileRepository: MobileRepository
) {
    suspend operator fun invoke(): List<Group> {
        val mobiles = mobileRepository.getPhoneContacts()
        val groups = groupRepository.getAllGroup()
        for(group in groups) {
            val list = group.contactList
            val newList = mutableListOf<Contact>()
            for(contact in list) {
                var target = contact
                var isFind = false
                for(mobile in mobiles) {
                    if (target.id == mobile.id) {
                        isFind = true
                        break
                    }
                }
                if (isFind) {
                    newList.add(target)
                }
            }
            groupRepository.update(group.copy(contactList = newList))

        }
        return groupRepository.getAllGroup()

    }
}
