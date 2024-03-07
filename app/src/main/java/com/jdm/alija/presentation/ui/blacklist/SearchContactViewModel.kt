package com.jdm.alija.presentation.ui.blacklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.BlackRepository
import com.jdm.alija.domain.usecase.GetAllContactUseCase
import com.jdm.alija.domain.usecase.GetAllSearchContactUseCase
import com.jdm.alija.domain.usecase.GetMobileUseCase
import com.jdm.alija.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchContactViewModel @Inject constructor(
    private val getMobileUseCase: GetMobileUseCase,
    private val blackRepository: BlackRepository,
    private val getAllSearchContactUseCase: GetAllSearchContactUseCase
) : ViewModel(){

    private var contactOriginData = mutableListOf<BlackContact>()
    val contactData: SingleLiveEvent<List<BlackContact>> = SingleLiveEvent()
    val isFinish: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun getContactList() {
        viewModelScope.launch {
            var contacts: List<BlackContact> = getAllSearchContactUseCase.invoke().map { BlackContact(mobile = it.mobile, name = it.name) }
            contactOriginData.addAll(contacts)
            contactData.value = contacts

        }


    }
    fun searchKeyword(keyword: String) {
        val filteredList = mutableListOf<BlackContact>()
        val nameFilter = contactOriginData.filter { it.name.contains(keyword) }
        val numberFilter = contactOriginData.filter { it.mobile.isNotEmpty() }.filter { it.mobile.contains(keyword) }
        filteredList.addAll(nameFilter)

        for(number in numberFilter) {
            var flag = true
            for(name in nameFilter) {
                if (name.mobile == number.mobile) {
                    flag = false
                    break
                }
            }
            if (flag) filteredList.add(number)
        }
        contactData.value = filteredList
    }
    fun clickContactItem(item: BlackContact, keyword: String) {
        contactOriginData.forEach {
            if (it.mobile == item.mobile) {
                it.isSelected = !item.isSelected
            }
        }
        searchKeyword(keyword)
    }
    private fun getSelectedList(): ArrayList<BlackContact> {
        val list = ArrayList<BlackContact>()
        list.addAll(contactOriginData.filter { it.isSelected })
        return list
    }
    fun insertBlackList() {
        val selectedList = getSelectedList()
        viewModelScope.launch {
            for(blackContact in selectedList) {
                blackRepository.insert(blackContact)
            }
            isFinish.value = true

        }
    }


}