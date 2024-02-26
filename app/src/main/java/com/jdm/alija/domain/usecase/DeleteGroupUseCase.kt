package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.toContract
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.repository.MobileRepository
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
    private val contractRepository: ContactRepository
) {
    suspend operator fun invoke(group: Group) {
        contractRepository.deleteContactByGroupName(group)
        groupRepository.delete(group)
    }
}
