package com.jdm.alija.presentation.ui.main

import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.presentation.ui.contract.ContractFragment
import com.jdm.alija.presentation.ui.group.GroupFragment
import com.jdm.alija.presentation.ui.home.HomeFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel<MainContract.MainViewState, MainContract.MainSideEffect, MainContract.MainEvent>(
    MainContract.MainViewState()
) {
    override fun handleEvents(event: MainContract.MainEvent) {
        when (event) {
            is MainContract.MainEvent.OnClickHomeButton -> {
                sendEffect({ MainContract.MainSideEffect.ShowFragment(HomeFragment.TAG) })
            }
            is MainContract.MainEvent.OnClickGroupButton -> {
                sendEffect({ MainContract.MainSideEffect.ShowFragment(GroupFragment.TAG) })
            }
            is MainContract.MainEvent.OnClickStartService -> {
                sendEffect({ MainContract.MainSideEffect.StartService })
            }
            is MainContract.MainEvent.OnClickStopService -> {
                sendEffect({ MainContract.MainSideEffect.StopService })
            }
        }
    }
}