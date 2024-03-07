package com.jdm.alija.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.usecase.CreateGroupUseCase
import com.jdm.alija.domain.usecase.GetAllGroupUseCase
import com.jdm.alija.presentation.ui.home.HomeFragment
import com.jdm.alija.presentation.ui.setting.SettingFragment
import com.jdm.alija.presentation.ui.msg.MessageFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase,
    private val getAllGroupUseCase: GetAllGroupUseCase,
    private val contactRepository: ContactRepository
): BaseViewModel<MainContract.MainViewState, MainContract.MainSideEffect, MainContract.MainEvent>(
    MainContract.MainViewState()
) {
    override fun handleEvents(event: MainContract.MainEvent) {
        when (event) {
            is MainContract.MainEvent.OnClickHomeButton -> {
                sendEffect({ MainContract.MainSideEffect.ShowFragment(HomeFragment.TAG) })
            }
            is MainContract.MainEvent.OnClickSettingButton -> {
                sendEffect({ MainContract.MainSideEffect.ShowFragment(SettingFragment.TAG) })
            }
            is MainContract.MainEvent.OnClickStartService -> {
                sendEffect({ MainContract.MainSideEffect.StartService })
            }
            is MainContract.MainEvent.OnClickStopService -> {
                sendEffect({ MainContract.MainSideEffect.StopService })
            }
        }
    }
    fun getDefaultGroup() {
        viewModelScope.launch {
            val groups = getAllGroupUseCase.invoke()
        }
    }
    fun deleteContact() {
        viewModelScope.launch {
            val list = contactRepository.getAllContact()
            list.forEach { contactRepository.deleteSms(it) }
        }
    }
}