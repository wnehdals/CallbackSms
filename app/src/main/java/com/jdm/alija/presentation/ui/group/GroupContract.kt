package com.jdm.alija.presentation.ui.group

import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class GroupContract {
    data class GroupViewState(
        val groupList: List<Group> = listOf()
    ) : ViewState

    sealed class GroupSideEffect : ViewSideEffect {
        data class ShowToast(val msg: String): GroupSideEffect()
    }

    sealed class GroupEvent : ViewEvent {
        data class OnClickComplete(val text: String) : GroupEvent()
    }
}