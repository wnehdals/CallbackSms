package com.jdm.alija.domain.usecase

import com.jdm.alija.domain.model.CallbackResult
import com.jdm.alija.domain.repository.ContactRepository
import javax.inject.Inject

class GetCallbackResultByMobileUseCase @Inject constructor(
    private val getMobileUseCase: GetMobileUseCase
) {
    suspend operator fun invoke(mobile: String) : CallbackResult {
        val contacts = getMobileUseCase.invoke()
        val callbackResult = CallbackResult(mobile)
        for(contact in contacts) {
            if (mobile == contact.mobile.replace("-","").trim()) {
                callbackResult.name = contact.name
                break
            }
        }
        return callbackResult
    }
}