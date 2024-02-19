package com.jdm.alija.presentation.ui.contractdetail

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jdm.alarmlocation.data.entity.SmsEntity
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.SmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactDetailViewModel @Inject constructor(
    private val smsRepository: SmsRepository
) : BaseViewModel<ContactDetailContract.ContactDetailViewState, ContactDetailContract.ContactDetailSideEffect, ContactDetailContract.ContactDetailEvent>(
    ContactDetailContract.ContactDetailViewState()
){
    override fun handleEvents(event: ContactDetailContract.ContactDetailEvent) {
         when (event) {
             is ContactDetailContract.ContactDetailEvent.OnClickComplete -> {
                 updateState { copy(text = event.text) }
                 saveContact()
             }
             is ContactDetailContract.ContactDetailEvent.OnClickAttachImg -> {
                 updateState { copy(imgUri = event.uri?.uri, text = event.text) }
             }
         }
    }
    fun initData(item: Contact?) {
        if (item == null) {
            sendEffect({ContactDetailContract.ContactDetailSideEffect.GoToBack})
        } else {
            val phone = if (item.numbers.isEmpty()) "" else item.numbers[0]
            Log.e("viewmodel", item.toString())
            val imgUri = if (item.imgUri.isEmpty() || item.imgUri == "null") null else Uri.parse(item.imgUri)
            updateState { copy(id = item.id, name = item.name, mobile = phone, imgUri = imgUri, text = item.text, isSelected = item.isSelected) }
        }
    }
    fun saveContact() {
        viewModelScope.launch {
            val smsEntity = SmsEntity(
                id = currentState.id,
                mobile = currentState.mobile,
                text = currentState.text,
                imgUri = currentState.imgUri.toString()
            )
            Log.e(TAG, smsEntity.toString())
            val result = smsRepository.update(smsEntity)
            sendEffect({
                ContactDetailContract.ContactDetailSideEffect.ShowToast("저장되었습니다.")
                ContactDetailContract.ContactDetailSideEffect.GoToBack
            })
        }
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}