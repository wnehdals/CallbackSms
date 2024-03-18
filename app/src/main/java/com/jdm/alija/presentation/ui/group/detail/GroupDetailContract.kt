package com.jdm.alija.presentation.ui.group.detail

import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.model.SelectData
import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class GroupDetailContract {
    data class GroupDetailViewState(
        /*
        val id: Int = -1,
        val groupName: String = "",
        val incallText: String = "",
        val incallImg: String = "",
        val outcallText: String = "",
        val outcallImg: String = "",
        val releaseCallText: String = "",
        val releaseCallImg: String = "",
        val isIncallActive: Boolean = false,
        val isOutcallActivie: Boolean = false,
        val isReleaseCallActive: Boolean = false,
        val duplicateList: List<SelectData> = listOf(
            SelectData(0,"1일/1회",false),
            SelectData(1,"1회/1주", false),
            SelectData(2,"1회/1달",false),
            SelectData(3,"제한없음",false)
        ),
        val dupicateIdx: Int = 0,
        val mon: Boolean = false,
        val tue: Boolean = false,
        val wed: Boolean = false,
        val thu: Boolean = false,
        val fri: Boolean = false,
        val sat: Boolean = false,
        val sun: Boolean = false,
        val contacts: List<Contact> = listOf(),
        val isBeforeCheck: Boolean = true,

         */
        val group: Group = Group(id =-1, name = "")
    ) : ViewState

    sealed class GroupDetailSideEffect : ViewSideEffect {
        data class ShowFragment(val tag: String) : GroupDetailSideEffect()
        object StartService: GroupDetailSideEffect()
        object StopService: GroupDetailSideEffect()
        data class GoToContact(val groupName: String): GroupDetailSideEffect()
        data class GoToMessageDetail(val text: String, val imgPath: String, val type: Int): GroupDetailSideEffect()
    }

    sealed class GroupDetailEvent : ViewEvent {
        data class OnClickContact(val groupName: String): GroupDetailEvent()
        data class UpdateGroupContact(val contact: ArrayList<Contact>): GroupDetailEvent()
        data class OnClickIncalActiveButton(val value: Boolean) : GroupDetailEvent()
        data class OnClickOutcalActiveButton(val value: Boolean) : GroupDetailEvent()
        data class OnClickReleasecalActiveButton(val value: Boolean) : GroupDetailEvent()
        data class OnClickDuplicate(val item: SelectData): GroupDetailEvent()
        data class OnClickDuplicate2(val item: SelectData): GroupDetailEvent()
        data class OnClickDuplicate3(val item: SelectData): GroupDetailEvent()
        data class OnClickDay(val idx: Int, val value: Boolean): GroupDetailEvent()
        data class OnClickBeforeCheck(val item: SelectData): GroupDetailEvent()
    }
}