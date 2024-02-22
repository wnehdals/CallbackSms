package com.jdm.alija.presentation.ui.contractdetail

import android.net.Uri
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Photo
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class ContactDetailContract {
    data class ContactDetailViewState(
        val id: String = "",
        val name: String = "",
        val mobile: String = "",
        val imgUri: Uri? = null,
        val text: String = "",
        val isSelected : Boolean = false,
        val isKakao: Boolean = false
    ) : ViewState

    sealed class ContactDetailSideEffect : ViewSideEffect {
        data class ShowToast(val msg: String): ContactDetailSideEffect()
        object GoToBack: ContactDetailSideEffect()
    }

    sealed class ContactDetailEvent : ViewEvent {
        data class OnClickComplete(val text: String) : ContactDetailEvent()
        data class OnClickAttachImg(val uri: Photo?, val text: String) : ContactDetailEvent()
    }
}