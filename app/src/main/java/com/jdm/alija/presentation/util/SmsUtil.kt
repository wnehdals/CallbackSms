package com.jdm.alija.presentation.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class SmsUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var smsManager: SmsManager
    init {
        smsManager = context.getSystemService(SmsManager::class.java) as SmsManager
    }
    fun sendImageMessage() {

    }
    fun sendTextMessage(mobile: String, text: String) {
        if (text.length > 70) {
            val part = smsManager.divideMessage(text)
            smsManager.sendMultipartTextMessage(mobile, null, part, null, null)
        } else {
            val sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
            val deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE)
            smsManager.sendTextMessage(mobile, null, text, sentPendingIntent, deliveredPendingIntent)
        }

    }
    fun sendImgMessage(mobile: String, bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        val deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE)
        smsManager.sendDataMessage(mobile, null, 8091,  byteArray, null, null)
        //smsManager.sendMultimediaMessage(context, contextUri, null, null, sentPendingIntent)
    }
}