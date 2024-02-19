package com.jdm.alija.presentation.ui.photoselect

import com.jdm.alija.domain.model.Photo
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState
import com.jdm.alija.presentation.ui.contractdetail.ContactDetailContract

class PhotoSelectContract {
    data class PhotoSelectViewState(
        val photoList: List<Photo> = listOf()
    ) : ViewState

    sealed class PhotoSelectSideEffect : ViewSideEffect {
        object GoToBack: PhotoSelectSideEffect()
    }

    sealed class PhotoSelectEvent : ViewEvent {
        data class OnClickPhotoSelect(val photo: Photo): PhotoSelectEvent()
    }
}