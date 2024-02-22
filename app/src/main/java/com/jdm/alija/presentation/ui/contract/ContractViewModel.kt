package com.jdm.alija.presentation.ui.contract

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jdm.alarmlocation.data.entity.SmsEntity
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.repository.SmsRepository
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.ContractRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    private val smsRepository: SmsRepository
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
            val contactListAsync = async {  contractRepository.getPhoneContacts()}
            val contactNumberAsync = async { contractRepository.getContactNumbers() }
            val smsListAsync = async { smsRepository.getAllSms() }
            val contacts = contactListAsync.await()
            val contactNumber = contactNumberAsync.await()
            val smsList = smsListAsync.await()
            contacts.forEach {
                contactNumber[it.id]?.let { numbers ->
                    it.numbers = numbers
                }
            }
            for(contact in contacts) {
                for(sms in smsList) {
                    if (contact.id == sms.id) {
                        contact.isSelected = true
                        contact.text = sms.text
                        contact.imgUri = sms.imgUri
                        contact.isKakao = sms.isKakao
                        break
                    }
                }
            }
            Log.e("tag", "viewmodel")
            contactOriginData = contacts
            if (keyword.isEmpty()) {
                _contactData.value = contacts
            } else {
                searchKeyword(keyword)
            }

        }

    }
    fun insertSms(item: Contact, keyword: String) {
        if (item.isSelected) {
            viewModelScope.launch {
                smsRepository.deleteSms(SmsEntity(item.id, "", "", "", false))
                getContractList(keyword)
            }
        } else {
            viewModelScope.launch {
                val number = if (item.numbers.isEmpty()) "" else item.numbers[0]
                smsRepository.insert(item.id, "", "", number, false)
                getContractList(keyword)
            }
        }
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}