package com.jdm.alija.domain.usecase

import com.jdm.alija.domain.model.CallbackResult
import com.jdm.alija.domain.repository.ContactRepository
import javax.inject.Inject

class GetCallbackHistoryUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val getMobileUseCase: GetMobileUseCase
) {
    suspend operator fun invoke() : List<CallbackResult> {
        val mobiles = getMobileUseCase.invoke()
        val contacts: List<CallbackResult> = contactRepository.getAllContact().map { contact ->
            CallbackResult(
                mobile = contact.mobile,
                year = contact.year,
                month = contact.month,
                day = contact.day,
                hour = contact.hour,
                minute = contact.minute,
            )
        }
        for(contact in contacts) {
            for (mobile in mobiles) {
                if (contact.mobile == mobile.mobile.replace("-","").trim()) {
                    contact.name = mobile.name
                    break
                }
            }
        }
        return contacts
    }
}