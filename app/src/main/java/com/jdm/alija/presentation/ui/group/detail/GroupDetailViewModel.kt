package com.jdm.alija.presentation.ui.group.detail

import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.model.SelectData
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.usecase.DeleteGroupUseCase
import com.jdm.alija.domain.usecase.GetDefaultGroupUseCase
import com.jdm.alija.domain.usecase.GetGroupByIdUseCase
import com.jdm.alija.domain.usecase.UpdateGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val deleteGroupUseCase: DeleteGroupUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase,
    private val getDefaultGroupUseCase: GetDefaultGroupUseCase,
    private val getGroupByIdUseCase: GetGroupByIdUseCase

) : BaseViewModel<GroupDetailContract.GroupDetailViewState, GroupDetailContract.GroupDetailSideEffect, GroupDetailContract.GroupDetailEvent>(
    GroupDetailContract.GroupDetailViewState()
){
    var id = -1
    override fun handleEvents(event: GroupDetailContract.GroupDetailEvent) {
        when (event) {
            is GroupDetailContract.GroupDetailEvent.OnClickContact -> {
                //sendEffect({ GroupDetailContract.GroupDetailSideEffect.GoToContact(currentState.groupName) })
            }
            is GroupDetailContract.GroupDetailEvent.UpdateGroupContact -> {
                updateContactList(event.contact)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickIncalActiveButton -> {
                updateIncallActive(event.value)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickOutcalActiveButton -> {
                updateOutcallActive(event.value)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickReleasecalActiveButton -> {
                updateReleasecallActive(event.value)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickDuplicate -> {
                updateDuplicateIdx(event.item.id)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickDay -> {
                updateDay(event.idx,event.value)
            }
            is GroupDetailContract.GroupDetailEvent.OnClickBeforeCheck -> {
                updateBeforeCheck(event.item)
            }
            else -> {}
        }
    }
    fun updateGroup() {
        viewModelScope.launch {
            updateGroupUseCase.execute(currentState.group)
        }
    }
    fun updateBeforeCheck(selectData: SelectData) {
        if (selectData.id == 0) {
            updateState { copy(group = group.copy(isBeforeCheck = true)) }
        } else {
            updateState { copy(group = group.copy(isBeforeCheck = false)) }
        }
        updateGroup()
    }
    fun updateDay(idx: Int, value: Boolean) {
        when (idx)  {
            0 -> {
                updateState { copy(group = group.copy(mon = !value)) }
            }
            1 -> {
                updateState { copy(group = group.copy(tue = !value)) }
            }
            2 -> {
                updateState { copy(group = group.copy(wed = !value)) }
            }
            3 -> {
                updateState { copy(group = group.copy(thu = !value)) }
            }
            4 -> {
                updateState { copy(group = group.copy(fri = !value)) }
            }
            5 -> {
                updateState { copy(group = group.copy(sat = !value)) }
            }
            6 -> {
                updateState { copy(group = group.copy(sun = !value)) }
            }

        }
        updateGroup()
    }
    fun updateDuplicateIdx(idx: Int) {
        updateState { copy(group = group.copy(dupicateIdx = idx)) }
        updateGroup()
    }
    fun updateIncallActive(value: Boolean) {
        updateState { copy(group = group.copy(isIncallActive = !value)) }
        updateGroup()
    }
    fun updateOutcallActive(value: Boolean) {
        updateState { copy(group = group.copy(isOutcallActivie = !value)) }
        updateGroup()
    }
    fun updateReleasecallActive(value: Boolean) {
        updateState { copy(group = group.copy(isReleaseCallActive = !value)) }
        updateGroup()
    }
    fun getGroup(groupId: Int) {
        id = groupId
        viewModelScope.launch {
            val group = getGroupByIdUseCase.invoke(groupId)

            updateState { copy(group = group) }
        }
    }
    fun getDefaultGroup() {
        viewModelScope.launch {
            val group = getDefaultGroupUseCase.invoke()
            id = group.id
            updateState { copy(group = group) }
            /*
            updateState { copy(
                id = group.id,
                groupName = group.name,
                incallText = group.incallText,
                incallImg = group.incallImg,
                outcallText = group.outcallText,
                outcallImg = group.outcallImg,
                releaseCallText = group.releaseCallText,
                releaseCallImg = group.releaseCallImg,
                isIncallActive = group.isIncallActive,
                isOutcallActivie = group.isOutcallActivie,
                isReleaseCallActive = group.isReleaseCallActive,
                dupicateIdx = group.dupicateIdx,
                mon = group.mon,
                tue = group.tue,
                wed = group.wed,
                thu = group.thu,
                fri = group.fri,
                sat = group.sat,
                sun = group.sun,
                contacts = group.contactList,
                isBeforeCheck = group.isBeforeCheck
            ) }

             */
        }
    }
    fun initData(group: Group) {
    }
    private fun updateMessage(type: Int, text: String, imgPath: String) {
    }
    private fun updateContactList(list: List<Contact>) {
        /*
        Log.e(TAG, list.toString())
        list.forEach {
            it.groupName = currentState.groupName
        }

         */
    }
    fun isContractSelected(): Boolean {
        return !currentState.group.contactList.isEmpty()

    }

    companion object {
        val TAG = this.javaClass.simpleName
    }


}