package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.toContract
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.BlackRepository
import com.jdm.alija.domain.repository.MobileRepository
import javax.inject.Inject

class GetAllSearchContactUseCase @Inject constructor(
    private val getMobileUseCase: GetMobileUseCase,
    private val blackRepository: BlackRepository
) {
    suspend operator fun invoke(): List<Contact>{
        val contacts = getMobileUseCase.invoke()
        val blacks = blackRepository.getAllContact()
        for(contact in contacts) {
            for(black in blacks) {
                if (contact.mobile == black.mobile) {
                    contact.isSelected = true
                }
            }
        }
        return contacts
    }
}
