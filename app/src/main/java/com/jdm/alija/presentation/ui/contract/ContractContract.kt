package com.jdm.alija.presentation.ui.contract

import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class ContractContract {
    data class ContractViewState(
        val contactList: List<Contact> = listOf()
    ) : ViewState

    sealed class ContractSideEffect : ViewSideEffect {
        object GoToBack: ContractSideEffect()
    }

    sealed class ContractEvent : ViewEvent {
    }
}