package com.jdm.alija

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.CALL_STATE_IDLE
import android.util.Log
import com.jdm.alija.presentation.service.SmsService
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.util.Const
import com.jdm.alija.presentation.util.Const.ACTION_SEND_SMS_SERVICE
import java.lang.Exception

class CallComplete : BroadcastReceiver() {
    private var step= 0
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val tm: TelephonyManager = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                tm.registerTelephonyCallback(
                    context.mainExecutor,
                    object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                        override fun onCallStateChanged(state: Int) {

                            when(state) {
                                TelephonyManager.CALL_STATE_OFFHOOK -> {
                                    step = 2
                                    Log.e(TAG, "off hook ${step}")
                                }
                                TelephonyManager.CALL_STATE_RINGING -> {
                                    step = 1
                                    Log.e(TAG, "ring  ${step}")
                                }
                                CALL_STATE_IDLE -> {
                                    if (step == 2) {
                                        step = 0
                                        val phoneNumber = intent.extras?.getString("incoming_number")
                                        if (isSmsServiceRunning(context)) {
                                            if (phoneNumber != null) {
                                                val intent2 = Intent(context, SmsService::class.java)
                                                intent2.setAction(ACTION_SEND_SMS_SERVICE)
                                                intent2.putExtra("mobile", phoneNumber)
                                                context.startService(intent2)
                                                //val intent3 = Intent(context, MainActivity::class.java)
                                                //intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                //context.startActivity(intent3)
                                                //context.startActivity(intent)
                                            }
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                )
            } else {
                tm.listen(object : PhoneStateListener() {
                    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                        when (state) {
                            CALL_STATE_IDLE -> {
                                val phoneNumber2 = intent.extras?.getString("incoming_number")
                                Log.e(TAG, "${Build.VERSION.SDK_INT}-${phoneNumber2}")
                                val smsUri = Uri.parse("smsto:" + phoneNumber)
                                val intent = Intent(Intent.ACTION_SENDTO)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.setData(smsUri)
                                intent.putExtra("sms_body","test")
                                context.startActivity(intent)
                            }
                            else -> {}
                        }

                    }
                }, PhoneStateListener.LISTEN_CALL_STATE)
            }
        } catch (e: Exception) {

        }
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
        val TAG = this.javaClass.simpleName
    }
}