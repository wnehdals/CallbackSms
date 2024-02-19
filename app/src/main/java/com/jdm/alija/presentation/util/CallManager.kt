package com.jdm.alija.presentation.util

import android.telecom.Call
import android.util.Log
import com.jdm.alija.domain.model.CallType
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

object CallManager {
    private const val LOG_TAG = "CallManager"
    private val subject = BehaviorSubject.create<CallType>()
    private var currentCall: Call? = null

    fun update(): Observable<CallType> = subject
    fun updateCall(call: Call?) {
        call?.let {
            subject.onNext(it.toCallType())
        }
    }
    fun cancelCall() {
        currentCall?.let {
            when (it.state) {
                Call.STATE_RINGING -> rejectCall()
                else               -> disconnectCall()
            }
        }
    }

    fun acceptCall() {
        Log.i(LOG_TAG, "acceptCall")
        currentCall?.let {
            it.answer(it.details.videoState)
        }
    }

    private fun rejectCall() {
        Log.i(LOG_TAG, "rejectCall")
        currentCall?.reject(false, "")
    }

    private fun disconnectCall() {
        Log.i(LOG_TAG, "disconnectCall")
        currentCall?.disconnect()
    }
}