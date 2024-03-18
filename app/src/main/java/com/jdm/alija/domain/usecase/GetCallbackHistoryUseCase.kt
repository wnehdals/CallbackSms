package com.jdm.alija.domain.usecase

import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.domain.model.CallbackResult
import com.jdm.alija.domain.model.Contact
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
                year = getYear(contact),
                month = getMonth(contact),
                day = getDay(contact),
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
    private fun getYear(contact: ContactEntity) : Int{
        var year = ""
        if (contact.year != 0)
            year += "${contact.year}"
        if (contact.year2 != 0)
            year += "${contact.year2}"
        if (contact.year3 != 0)
            year += "${contact.year3}"
        return year.toInt()
    }
    private fun getMonth(contact: ContactEntity) : Int{
        var year = ""
        if (contact.month != 0)
            year += "${contact.month}"
        if (contact.month2 != 0)
            year += "${contact.month2}"
        if (contact.month3 != 0)
            year += "${contact.month3}"
        return year.toInt()
    }
    private fun getDay(contact: ContactEntity) : Int{
        var year = ""
        if (contact.day != 0)
            year += "${contact.day}"
        if (contact.day2 != 0)
            year += "${contact.day2}"
        if (contact.day3 != 0)
            year += "${contact.day3}"
        return year.toInt()
    }
}