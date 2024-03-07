package com.jdm.alija.presentation.ui.msg.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.usecase.GetAllGroupUseCase
import com.jdm.alija.domain.usecase.GetGroupByIdUseCase
import com.jdm.alija.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageSendViewModel @Inject constructor(
    private val getGroupByIdUseCase: GetGroupByIdUseCase
): ViewModel(){
    val text: SingleLiveEvent<String> = SingleLiveEvent()
    val imgPath: SingleLiveEvent<String> = SingleLiveEvent()
    lateinit var group: Group
    fun getGroup(groupId: Int, type: Int) {
        viewModelScope.launch {
            group = getGroupByIdUseCase.invoke(groupId)
            when (type) {
                MessageSendActivity.INCALL -> {
                    text.value = group.incallText
                    imgPath.value = group.incallImg
                }
                MessageSendActivity.OUTCALL -> {
                    text.value = group.outcallText
                    imgPath.value = group.outcallImg
                }
                MessageSendActivity.RELEASE -> {
                    text.value = group.releaseCallText
                    imgPath.value = group.releaseCallImg
                }
            }

        }
    }
}