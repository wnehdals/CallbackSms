package com.jdm.alija.presentation.ui.msg

import com.jdm.alija.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(

) : BaseViewModel<MessageContract.MessageViewState, MessageContract.MessageSideEffect, MessageContract.MessageEvent>(
    MessageContract.MessageViewState()
){
    override fun handleEvents(event: MessageContract.MessageEvent) {
        when(event) {
            is MessageContract.MessageEvent.OnClickContactX -> {
                updateState { copy(contactX = !event.value) }
            }
            is MessageContract.MessageEvent.OnClickContactO -> {
                updateState { copy(contactO = !event.value) }
            }
        }
    }
}