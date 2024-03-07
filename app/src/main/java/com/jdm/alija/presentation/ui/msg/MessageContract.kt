package com.jdm.alija.presentation.ui.msg

import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class MessageContract {
    data class MessageViewState(
        val contactX: Boolean = false,
        val contactO: Boolean = false
    ) : ViewState

    sealed class MessageSideEffect : ViewSideEffect {
        data class ShowFragment(val tag: String) : MessageSideEffect()
        object StopService: MessageSideEffect()
    }

    sealed class MessageEvent : ViewEvent {
        data class OnClickContactX(val value: Boolean) : MessageEvent()
        data class OnClickContactO(val value: Boolean) : MessageEvent()
    }
}