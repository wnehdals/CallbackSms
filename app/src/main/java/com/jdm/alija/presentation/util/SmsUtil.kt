package com.jdm.alija.presentation.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.telephony.SmsManager
import androidx.core.os.bundleOf
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import com.klinker.android.send_message.Transaction
import javax.inject.Inject


class SmsUtil @Inject constructor(
) {
    /*
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

     */
    fun sendSms(context: Context, mobile: String, text: String, imgPath: String) {
        val settings = Settings()
        settings.useSystemSending = true
        val transaction: Transaction = Transaction(context, settings)
        val message = Message(text, mobile,"")
        if (imgPath.isEmpty() || imgPath == "null") {

        } else {
            val bitmap = BitmapFactory.decodeFile(imgPath)
            message.addImage(bitmap)
        }
        val id: Long = android.os.Process.getThreadPriority(android.os.Process.myTid()).toLong()
        transaction.sendNewMessage(message, id)
    }

}