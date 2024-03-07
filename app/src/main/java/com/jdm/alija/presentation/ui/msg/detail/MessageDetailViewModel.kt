package com.jdm.alija.presentation.ui.msg.detail

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.jdm.alija.R
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.usecase.GetGroupByIdUseCase
import com.jdm.alija.domain.usecase.UpdateGroupUseCase
import com.jdm.alija.presentation.ui.msg.MessageContract
import com.jdm.alija.presentation.ui.msg.MessageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MessageDetailViewModel @Inject constructor(
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase

) : BaseViewModel<MessageDetailContract.MessageDetailViewState, MessageDetailContract.MessageDetailSideEffect, MessageDetailContract.MessageDetailEvent>(
    MessageDetailContract.MessageDetailViewState()
){
    var type = -1
    var group: Group? = null
    override fun handleEvents(event: MessageDetailContract.MessageDetailEvent) {
        when (event) {
            is MessageDetailContract.MessageDetailEvent.OnClickDeleteImg -> {
                updateState { copy(imgPath = "", uri = null, text = event.text) }
            }
            else -> {}
        }
    }
    fun getGroup(id: Int, type: Int) {
        this.type = type
        viewModelScope.launch {
            group = getGroupByIdUseCase.invoke(id)
            when (type) {
                MessageDetailActivity.TYPE_INCALL -> {
                    updateState { copy(imgPath = group!!.incallImg, text = group!!.incallText) }
                }
                MessageDetailActivity.TYPE_OUTCALL -> {
                    updateState { copy(imgPath = group!!.outcallImg, text = group!!.outcallText) }
                }
                MessageDetailActivity.TYPE_RELEASECALL -> {
                    updateState { copy(imgPath = group!!.releaseCallImg, text = group!!.releaseCallText) }
                }
            }
        }
    }
    fun updateUri(uri: Uri) {
        updateState { copy(uri = uri) }
    }
    fun updateGroup(text: String, imgPath: String) {

        when (type) {
            MessageDetailActivity.TYPE_INCALL -> {
                group = group!!.copy(incallText = text, incallImg = imgPath)
            }
            MessageDetailActivity.TYPE_OUTCALL -> {
                group = group!!.copy(outcallText = text, outcallImg = imgPath)
            }
            MessageDetailActivity.TYPE_RELEASECALL -> {
                group = group!!.copy(releaseCallText = text, releaseCallImg = imgPath)
            }
        }
        viewModelScope.launch {
            updateGroupUseCase.execute(group!!)
            sendEffect({ MessageDetailContract.MessageDetailSideEffect.GoToBack })
        }

    }
}