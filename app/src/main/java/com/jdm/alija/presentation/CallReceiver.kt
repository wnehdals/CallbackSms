package com.jdm.alija.presentation

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.jdm.alija.presentation.service.SmsService
import com.jdm.alija.presentation.util.Const


class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null) {
            when (intent.action) {
                "android.intent.action.PHONE_STATE" -> handlePhoneState(context, intent)
                "android.intent.action.NEW_OUTGOING_CALL" -> handleOutgoingCall(context, intent, null)
            }
        }
    }

    private fun handlePhoneState(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state != null) {
            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    Log.e("state", "ring")
                    incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    ring = true
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    callReceived = true
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    if (incomingNumber == null)
                        return
                    Log.e("callreceiver", "ring : ${ring} / callreceived : ${callReceived} / incommingNmber : ${incomingNumber}")
                    var mobile = incomingNumber
                    if (ring == true && callReceived == false) {
                        handleReleaseCall(context, intent, mobile)

                    } else  if (ring == true && callReceived == true){
                        handleIncomingCall(context, intent, mobile)
                    } else if (ring == false && callReceived == true) {
                        handleOutgoingCall(context, intent, mobile)
                    }
                    ring = false
                    callReceived = false
                    incomingNumber = null
                }
            }
        }
    }

    private fun handleIncomingCall(context: Context, intent: Intent,  incomingNumber: String?) {
        if (incomingNumber.isNullOrEmpty())
            return
        val intent = Intent(context, SmsService::class.java)
        intent.setAction(Const.ACTION_SEND_INCALL_SERVICE)
        intent.putExtra("mobile",incomingNumber )

        if (isSmsServiceRunning(context))
            context.startService(intent)
    }

    private fun handleOutgoingCall(context: Context, intent: Intent, incomingNumber: String?) {
        if (incomingNumber.isNullOrEmpty())
            return
        Log.e("handleoutgoingcall", "ring : ${ring} / callreceived : ${callReceived} / incommingNmber : ${incomingNumber}")
        val intent = Intent(context, SmsService::class.java)
        intent.setAction(Const.ACTION_SEND_OUTCALL_SERVICE)
        intent.putExtra("mobile",incomingNumber )
        if (isSmsServiceRunning(context))
            context.startService(intent)
    }
    private fun handleReleaseCall(context: Context, intent: Intent, incomingNumber: String?) {
        if (incomingNumber.isNullOrEmpty())
            return
        val intent = Intent(context, SmsService::class.java)
        intent.setAction(Const.ACTION_SEND_RELEASE_SERVICE)
        intent.putExtra("mobile",incomingNumber )
        if (isSmsServiceRunning(context))
            context.startService(intent)
    }

    private fun isSmsServiceRunning(context: Context): Boolean {
        val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(Int.MAX_VALUE).forEach {
            if (it.service.className == Const.SERVICE_NAME) {
                if (it.foreground) {
                    return true
                }
            } else {
                return false
            }
        }
        return false
    }
    companion object {
        var ring = false
        var callReceived = false
        var incomingNumber: String? = null
    }
}