package com.jdm.alija.presentation.ui.contractdetail

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jdm.alarmlocation.data.entity.SmsEntity
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.repository.SmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
                 updateState { copy(imgPath = event.path, text = event.text) }
             }
             is ContactDetailContract.ContactDetailEvent.OnClickRadioGroup -> {
                 updateState { copy(isKakao = event.isKakao) }
             }
             is ContactDetailContract.ContactDetailEvent.OnClickHidden -> {
                 updateState { copy(isHidden = !event.value) }
             }
         }
    }
    fun initData(item: Contact?) {
        if (item == null) {
            sendEffect({ContactDetailContract.ContactDetailSideEffect.GoToBack})
        } else {
            val phone = if (item.numbers.isEmpty()) "" else item.numbers[0]
            val imgPath: String? = if (item.imgUri.isEmpty() || item.imgUri == "null") null else item.imgUri
            updateState { copy(id = item.id, name = item.name, mobile = phone, imgPath = imgPath, text = item.text, isSelected = item.isSelected, isKakao = item.isKakao, isHidden = item.isHidden) }
        }
    }
    fun saveContact() {
        viewModelScope.launch {
            val smsEntity = SmsEntity(
                id = currentState.id,
                mobile = currentState.mobile,
                text = currentState.text,
                imgUri = currentState.imgPath?: "",
                isKakao = currentState.isKakao,
                isHidden = currentState.isHidden
            )
            Log.e(TAG, smsEntity.toString())
            val result = smsRepository.update(smsEntity)
            sendEffect({
                ContactDetailContract.ContactDetailSideEffect.ShowToast("저장되었습니다.")
            })
            delay(1000L)
            sendEffect({
                ContactDetailContract.ContactDetailSideEffect.GoToBack
            })
        }
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}