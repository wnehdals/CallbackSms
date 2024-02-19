package com.jdm.alija.presentation.ui.main

import com.jdm.alija.domain.model.ViewEvent
import com.jdm.alija.domain.model.ViewSideEffect
import com.jdm.alija.domain.model.ViewState

class MainContract {
    data class MainViewState(
        val tag: String = ""
    ) : ViewState

    sealed class MainSideEffect : ViewSideEffect {
        data class ShowFragment(val tag: String) : MainSideEffect()
        object StartService: MainSideEffect()
        object StopService: MainSideEffect()
    }

    sealed class MainEvent : ViewEvent {
        object OnClickHomeButton : MainEvent()
        object OnClickContractButton : MainEvent()
        object OnClickStartService: MainEvent()
        object OnClickStopService: MainEvent()
    }
}