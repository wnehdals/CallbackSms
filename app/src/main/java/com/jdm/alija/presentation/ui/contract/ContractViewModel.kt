package com.jdm.alija.presentation.ui.contract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.MobileRepository
import com.jdm.alija.domain.usecase.GetAllContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractViewModel @Inject constructor(
    val getAllContactUseCase: GetAllContactUseCase
) : BaseViewModel<ContractContract.ContractViewState, ContractContract.ContractSideEffect, ContractContract.ContractEvent>(
    ContractContract.ContractViewState()
) {
    private var contactOriginData = listOf<Contact>()
    private val _contactData = MutableLiveData<List<Contact>>()
    val contactData: LiveData<List<Contact>> get() = _contactData
    override fun handleEvents(event: ContractContract.ContractEvent) {

    }
    fun searchKeyword(keyword: String) {
        val filteredList = mutableListOf<Contact>()
        val nameFilter = contactOriginData.filter { it.name.contains(keyword) }
        val numberFilter = contactOriginData.filter { it.numbers.isNotEmpty() }.filter { it.numbers[0].contains(keyword) }
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
        _contactData.value = filteredList
    }

    fun getContractList(keyword: String = "") {
        viewModelScope.launch {
            val contacts = getAllContactUseCase.invoke()
            contactOriginData = contacts
            if (keyword.isEmpty()) {
                _contactData.value = contacts
            } else {
                searchKeyword(keyword)
            }

        }

    }
    fun allCheck(isSelected: Boolean) {
        for(contact in contactOriginData) {
            if (contact.isEnable) {
                contact.isSelected = isSelected
            }
        }
        _contactData.value = contactOriginData



    }

    /*
    fun insertSms(item: Contact, keyword: String) {
        if (item.isSelected) {
            viewModelScope.launch {
                smsRepository.deleteSms(SmsEntity(item.id, "", "", "", false, false))
                getContractList(keyword)
            }
        } else {
            viewModelScope.launch {
                val number = if (item.numbers.isEmpty()) "" else item.numbers[0]
                smsRepository.insert(item.id, "", "", number, false, false)
                getContractList(keyword)
            }
        }
    }

     */
    companion object {
        val TAG = this.javaClass.simpleName
    }
}