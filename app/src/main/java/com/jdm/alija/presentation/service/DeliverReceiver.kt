package com.jdm.alija.presentation.service

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.jdm.alija.data.AppDatabase
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.presentation.util.Const
import com.klinker.android.send_message.MmsSentReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar


class DeliverReceiver: MmsSentReceiver() {
    override fun onMessageStatusUpdated(context: Context?, intent: Intent, resultCode: Int) {

        when (resultCode) {
            RESULT_OK -> {
                val newIntent = Intent(context, SmsService::class.java)
                newIntent.setAction(Const.ACTION_SAVE_CONTACT)
                context?.startService(newIntent)
            } else -> {
                Firebase.crashlytics.log("callback error code : ${resultCode}")
            }
        }
    }

    override fun updateInInternalDatabase(context: Context?, intent: Intent?, resultCode: Int) {
        //super.updateInInternalDatabase(context, intent, resultCode)
    }
}