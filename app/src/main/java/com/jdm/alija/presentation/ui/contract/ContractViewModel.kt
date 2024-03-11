package com.jdm.alija.presentation.ui.contract

import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.usecase.CreateGroupUseCase
import com.jdm.alija.domain.usecase.GetAllContactUseCase
import com.jdm.alija.domain.usecase.GetGroupByIdUseCase
import com.jdm.alija.domain.usecase.UpdateGroupUseCase
import com.jdm.alija.presentation.util.SingleLiveEvent
import com.jdm.alija.presentation.util.SoundSearcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractViewModel @Inject constructor(
    private val getAllContactUseCase: GetAllContactUseCase,
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase
) : BaseViewModel<ContractContract.ContractViewState, ContractContract.ContractSideEffect, ContractContract.ContractEvent>(
    ContractContract.ContractViewState()
) {
    lateinit var group: Group
    private var contactOriginData = mutableListOf<Contact>()
    val contactData: SingleLiveEvent<List<Contact>> = SingleLiveEvent()

    val indexList = mutableListOf<String>(
        "ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"
    )
    val indexMap = hashMapOf<Int, String>(
        0 to "가", 1 to "나", 2 to "다", 3 to "라", 4 to "마", 5 to "바", 6 to "사", 7 to "아",
        8 to "자", 9 to "차", 10 to "카", 11 to "타", 12 to "파", 13 to "하"
    )

    override fun handleEvents(event: ContractContract.ContractEvent) {

    }
    fun searchKeyword(keyword: String) {
        val filteredList = mutableListOf<Contact>()
        val nameFilter = contactOriginData.filter { SoundSearcher.matchString(it.name, keyword) }
        val numberFilter = contactOriginData.filter { it.mobile.isNotEmpty() }.filter { it.mobile.contains(keyword) }
        filteredList.addAll(nameFilter)

        for(number in numberFilter) {
            var flag = true
            for(name in nameFilter) {
                if (name.id == number.id) {
                    flag = false
                    break
                }
            }
            if (flag) filteredList.add(number)
        }
        contactData.value = filteredList
    }
    fun getGroup(groupId: Int) {
        viewModelScope.launch {
            group = getGroupByIdUseCase.invoke(groupId)
            val contacts = getAllContactUseCase.invoke()

            contacts.forEach {
                if (it.groupName == group.name) {
                    it.isEnable = true
                }
            }
            contactOriginData.addAll(contacts)
            contactData.value = contacts
        }
    }
    fun updateGroup() {
        viewModelScope.launch {
            val updateGroup = group.copy(contactList = getSelectedList())
            updateGroupUseCase.execute(updateGroup)
            sendEffect({ContractContract.ContractSideEffect.GoToBack})
        }
    }

    private fun getContractList(groupName: String) {
        viewModelScope.launch {
            val contacts = getAllContactUseCase.invoke()
            contacts.forEach {
                if (it.groupName == groupName) {
                    it.isEnable = true
                }
            }
            contactOriginData.addAll(contacts)
            contactData.value = contacts
        }

    }
    fun clickAll(isSelected: Boolean, keyword: String) {
        if (contactData.value == null)
            return
        for(contact in contactData.value!!) {
            if (contact.isEnable) {
                for(origin in contactOriginData) {
                    if (contact.id == origin.id) {
                        origin.isSelected = isSelected
                        break
                    }
                }
            }
        }
        searchKeyword(keyword)
    }
    fun clickContactItem(item: Contact, keyword: String) {
        contactOriginData.forEach {
            if (it.id == item.id) {
                it.isSelected = !item.isSelected
            }
        }
        searchKeyword(keyword)
    }



    private fun getSelectedList(): ArrayList<Contact> {
        val list = ArrayList<Contact>()
        list.addAll(contactOriginData.filter { it.isSelected && it.isEnable })
        list.forEach { it.groupName = group.name }
        return list
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}