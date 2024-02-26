package com.jdm.alija.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast


class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null) {
            when (intent.action) {
                "android.intent.action.PHONE_STATE" -> handlePhoneState(context, intent)
                "android.intent.action.NEW_OUTGOING_CALL" -> handleOutgoingCall(context, intent)
            }
        }
    }

    private fun handlePhoneState(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state != null) {
            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> handleIncomingCall(context, intent)
                TelephonyManager.EXTRA_STATE_IDLE -> handleCallIdle(context, intent)
            }
        }
    }

    private fun handleIncomingCall(context: Context, intent: Intent) {
        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        Log.d("CallReceiver", "Incoming call from: $incomingNumber")

        // 여기에 전화 수신 처리 코드를 추가하세요.
        // 예를 들어, 토스트 메시지를 표시할 수 있습니다.
        Toast.makeText(context, "Incoming call from: $incomingNumber", Toast.LENGTH_SHORT).show()
    }

    private fun handleOutgoingCall(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
        Log.d("CallReceiver", "Outgoing call to: $phoneNumber")

        // 여기에 전화 발신 처리 코드를 추가하세요.
        // 예를 들어, 토스트 메시지를 표시할 수 있습니다.
        Toast.makeText(context, "Outgoing call to: $phoneNumber", Toast.LENGTH_SHORT).show()
    }

    private fun handleCallIdle(context: Context, intent: Intent) {
        Log.d("CallReceiver", "Call idle")

        // 여기에 전화 부재중 처리 코드를 추가하세요.
        // 예를 들어, 토스트 메시지를 표시할 수 있습니다.
        Toast.makeText(context, "Call idle", Toast.LENGTH_SHORT).show()
    }
}