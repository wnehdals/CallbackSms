package com.jdm.alija.presentation.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import com.google.android.mms.ContentType
import com.google.android.mms.pdu_alt.CharacterSets
import com.google.android.mms.pdu_alt.EncodedStringValue
import com.google.android.mms.pdu_alt.PduBody
import com.google.android.mms.pdu_alt.PduComposer
import com.google.android.mms.pdu_alt.PduPart
import com.google.android.mms.pdu_alt.SendReq
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.commons.io.FileUtils
import java.io.File
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
        val sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        val deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE)
        smsManager.sendTextMessage(mobile, null, text, sentPendingIntent, deliveredPendingIntent)
    }
    fun sendImgMessage(contextUri: Uri) {
        val sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
        val deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE)

        smsManager.sendMultimediaMessage(context, contextUri, null, null, sentPendingIntent)
    }
    private fun buildPdu(file: File): ByteArray? {
        val sendRequestPdu = SendReq()
        sendRequestPdu.addTo(EncodedStringValue("555-555-5555"))
        val pduBody = PduBody()
        val sampleImageData: ByteArray = FileUtils.readFileToByteArray(file)
        val sampleImagePduPart = PduPart()
        sampleImagePduPart.data = sampleImageData
        sampleImagePduPart.contentType = "image/png".toByteArray()
        //sampleImagePduPart.filename = file.name.getBytes()
        pduBody.addPart(sampleImagePduPart)
        sendRequestPdu.body = pduBody
        val composer = PduComposer(context, sendRequestPdu)
        val pduData = composer.make()
        return pduData
    }
}