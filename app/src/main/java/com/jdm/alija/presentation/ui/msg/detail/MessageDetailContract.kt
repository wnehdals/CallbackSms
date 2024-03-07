package com.jdm.alija.presentation.ui.msg.detail

import android.net.Uri
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class MessageDetailContract {
    data class MessageDetailViewState(
        val imgPath: String? = null,
        val text: String = "",
        val uri: Uri? = null
    ) : ViewState

    sealed class MessageDetailSideEffect : ViewSideEffect {
        data class ShowToast(val msg: String): MessageDetailSideEffect()
        object GoToBack: MessageDetailSideEffect()
    }

    sealed class MessageDetailEvent : ViewEvent {
        data class OnClickComplete(val text: String) : MessageDetailEvent()
        data class OnClickDeleteImg(val text: String) : MessageDetailEvent()
    }
}