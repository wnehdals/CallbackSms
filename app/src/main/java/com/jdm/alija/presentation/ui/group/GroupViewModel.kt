package com.jdm.alija.presentation.ui.group

import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.usecase.DeleteGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val deleteGroupUseCase: DeleteGroupUseCase

) : BaseViewModel<GroupContract.GroupViewState, GroupContract.GroupSideEffect, GroupContract.GroupEvent>(
    GroupContract.GroupViewState()
){
    override fun handleEvents(event: GroupContract.GroupEvent) {

    }
    fun getGroupList() {
        viewModelScope.launch {
            val data = groupRepository.getAllGroup()
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
            groupRepository.insert(name)
            getGroupList()
        }
    }
}