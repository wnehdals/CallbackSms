package com.jdm.alija.presentation.ui.blacklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.BlackRepository
import com.jdm.alija.domain.usecase.GetAllContactUseCase
import com.jdm.alija.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlackListViewModel @Inject constructor(
    private val getAllContactUseCase: GetAllContactUseCase,
    private val blackRepository: BlackRepository
) : ViewModel(){
    val contactList = SingleLiveEvent<List<Contact>>()
    val blackList = SingleLiveEvent<List<BlackContact>>()

    fun getAllContact() {
        viewModelScope.launch {
            contactList.value = getAllContactUseCase.invoke()
        }
    }
    fun getBlackList() {
        viewModelScope.launch {
            blackList.value = blackRepository.getAllContact()
        }
    }
    fun deleteBlackList(item: BlackContact) {
        viewModelScope.launch {
            blackRepository.deleteSms(item)
            getBlackList()
        }
    }
}