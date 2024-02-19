package com.jdm.alija

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class AlijaInCallServiceImpl : InCallService() {

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        if (call == null)
            return
        call.registerCallback(callCallback)
        Log.e("service", "onCall add")
        //val intent = Intent(this, CallActivity::class.java)
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //startActivity(intent)
        //CallManager.updateCall(call)
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        if (call == null)
            return
        call.unregisterCallback(callCallback)
        //CallManager.updateCall(null)
    }
    private val callCallback = object : Call.Callback() {

        override fun onStateChanged(call: Call?, state: Int) {
            //CallManager.updateCall(call)
            when(state) {
                Call.STATE_DISCONNECTED -> {
                    val phoneNumber2 = call?.details?.extras?.getString("incoming_number")
                    Log.e(CallComplete.TAG, "${Build.VERSION.SDK_INT}-${phoneNumber2}")
                    val smsUri = Uri.parse("smsto:" + phoneNumber2)
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setData(smsUri)
                    intent.putExtra("sms_body","test")
                    startActivity(intent)
                }
            }
        }
    }
}