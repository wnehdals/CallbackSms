package com.jdm.alija.presentation.ui.group

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.usecase.CreateGroupUseCase
import com.jdm.alija.domain.usecase.DeleteGroupUseCase
import com.jdm.alija.domain.usecase.GetAllGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val getAllGroupUseCase: GetAllGroupUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase

) : BaseViewModel<GroupContract.GroupViewState, GroupContract.GroupSideEffect, GroupContract.GroupEvent>(
    GroupContract.GroupViewState()
){
    override fun handleEvents(event: GroupContract.GroupEvent) {

    }
    fun getGroupList() {
        viewModelScope.launch {
            var data = getAllGroupUseCase.invoke()
            data = data.toMutableList()
            data = data.filter { it.id != GroupEntity.DEFAULT_GROUP_ID }
            updateState { copy(groupList = data) }
        }
    }
    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            deleteGroupUseCase.invoke(group)
            getGroupList()
        }
    }
    fun insertGroup(name: String) {
        viewModelScope.launch {
            createGroupUseCase.invoke(name)
            val groups = getAllGroupUseCase()
            Log.e("dsfs", groups.toString())
            sendEffect({GroupContract.GroupSideEffect.GoToGroupDetail(groups[groups.size-1])})
        }
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}