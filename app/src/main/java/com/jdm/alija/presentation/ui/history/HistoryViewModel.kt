package com.jdm.alija.presentation.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdm.alija.domain.model.CallbackResult
import com.jdm.alija.domain.usecase.GetCallbackHistoryUseCase
import com.jdm.alija.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getCallbackHistory: GetCallbackHistoryUseCase
): ViewModel() {
    val callbackHistory = SingleLiveEvent<List<CallbackResult>>()
    fun getCallbackResult() {
        viewModelScope.launch {
            val list = getCallbackHistory.invoke()
            callbackHistory.value = list
        }
    }
}