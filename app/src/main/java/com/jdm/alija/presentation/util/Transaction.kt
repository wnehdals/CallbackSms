package com.jdm.alija.presentation.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.text.TextUtils
import com.android.mms.MmsConfig
import com.android.mms.dom.smil.parser.SmilXmlSerializer
import com.android.mms.service_alt.MmsNetworkManager
import com.android.mms.service_alt.MmsRequestManager
import com.android.mms.service_alt.SendRequest
import com.android.mms.transaction.MmsMessageSender
import com.android.mms.util.DownloadManager
import com.android.mms.util.RateController
import com.google.android.mms.InvalidHeaderValueException
import com.google.android.mms.MMSPart
import com.google.android.mms.MmsException
import com.google.android.mms.pdu_alt.EncodedStringValue
import com.google.android.mms.pdu_alt.PduBody
import com.google.android.mms.pdu_alt.PduComposer
import com.google.android.mms.pdu_alt.PduPart
import com.google.android.mms.pdu_alt.PduPersister
import com.google.android.mms.pdu_alt.SendReq
import com.google.android.mms.smil.SmilHelper
import com.jdm.alija.domain.model.MessageInfo
import com.klinker.android.logger.Log
import com.klinker.android.send_message.BroadcastUtils
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import com.klinker.android.send_message.SmsManagerFactory
import com.klinker.android.send_message.StripAccents
import com.klinker.android.send_message.Utils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays
import java.util.Calendar
import java.util.Random

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


class Transaction @JvmOverloads constructor(context: Context, settings: Settings = Settings()) {
    private val context: Context
    private var explicitSentSmsReceiver: Intent? = null
    private var explicitSentMmsReceiver: Intent? = null
    private var explicitDeliveredSmsReceiver: Intent? = null
    private var saveMessage = true
    var SMS_SENT = ".SMS_SENT"
    var SMS_DELIVERED = ".SMS_DELIVERED"

    init {
        Transaction.settings = settings
        this.context = context
        SMS_SENT = context.packageName + SMS_SENT
        SMS_DELIVERED = context.packageName + SMS_DELIVERED
        if (NOTIFY_SMS_FAILURE == ".NOTIFY_SMS_FAILURE") {
            NOTIFY_SMS_FAILURE = context.packageName + NOTIFY_SMS_FAILURE
        }
    }

    @JvmOverloads
    fun sendNewMessage(
        message: Message,
        threadId: Long,
        sentMessageParcelable: Parcelable = Bundle(),
        deliveredParcelable: Parcelable = Bundle()
    ) {
        saveMessage = message.save
        if (checkMMS(message)) {
            try {
                Looper.prepare()
            } catch (var7: Exception) {
            }
            RateController.init(context)
            DownloadManager.init(context)
            sendMmsMessage(
                message.text,
                message.addresses,
                message.images,
                message.imageNames,
                message.parts,
                message.subject
            )
        } else {
            sendSmsMessage(
                message.text,
                message.addresses,
                threadId,
                message.delay,
                sentMessageParcelable,
                deliveredParcelable
            )
        }
    }

    fun setExplicitBroadcastForSentSms(intent: Intent?): Transaction {
        explicitSentSmsReceiver = intent
        return this
    }

    fun setExplicitBroadcastForSentMms(intent: Intent?): Transaction {
        explicitSentMmsReceiver = intent
        return this
    }

    fun setExplicitBroadcastForDeliveredSms(intent: Intent?): Transaction {
        explicitDeliveredSmsReceiver = intent
        return this
    }

    private fun sendSmsMessage(
        text: String,
        addresses: Array<String>,
        threadId: Long,
        delay: Int,
        sentMessageParcelable: Parcelable,
        deliveredParcelable: Parcelable
    ) {
        var text = text
        var threadId = threadId
        Log.v("send_transaction", "message text: $text")
        var messageUri: Uri? = null
        var messageId = 0
        if (saveMessage) {
            Log.v("send_transaction", "saving message")
            if (settings.signature != "") {
                text = """
                    $text
                    ${settings.signature}
                    """.trimIndent()
            }
            for (i in addresses.indices) {
                val cal = Calendar.getInstance()
                val values = ContentValues()
                values.put("address", addresses[i])
                values.put(
                    "body",
                    if (settings.stripUnicode) StripAccents.stripAccents(text) else text
                )
                values.put("date", cal.timeInMillis.toString() + "")
                values.put("read", 1)
                values.put("type", 4)
                if (threadId == 0L || addresses.size > 1) {
                    threadId = Utils.getOrCreateThreadId(
                        context,
                        addresses[i]
                    )
                }
                Log.v(
                    "send_transaction",
                    "saving message with thread id: $threadId"
                )
                values.put("thread_id", threadId)
                messageUri = context.contentResolver.insert(Uri.parse("content://sms/"), values)
                Log.v("send_transaction", "inserted to uri: $messageUri")
                val query = context.contentResolver.query(
                    messageUri!!,
                    arrayOf("_id"),
                    null as String?,
                    null as Array<String?>?,
                    null as String?
                )
                if (query != null && query.moveToFirst()) {
                    messageId = query.getInt(0)
                    query.close()
                }
                Log.v("send_transaction", "message id: $messageId")
                var sentIntent: Intent?
                if (explicitSentSmsReceiver == null) {
                    sentIntent = Intent(SMS_SENT)
                    BroadcastUtils.addClassName(context, sentIntent, SMS_SENT)
                } else {
                    sentIntent = explicitSentSmsReceiver
                }
                sentIntent!!.putExtra("message_uri", messageUri?.toString() ?: "")
                sentIntent.putExtra(
                    "com.klinker.android.send_message.SENT_SMS_BUNDLE",
                    sentMessageParcelable
                )
                val sentPI = PendingIntent.getBroadcast(
                    context, messageId,
                    sentIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
                var deliveredIntent: Intent?
                if (explicitDeliveredSmsReceiver == null) {
                    deliveredIntent = Intent(SMS_DELIVERED)
                    BroadcastUtils.addClassName(context, deliveredIntent, SMS_DELIVERED)
                } else {
                    deliveredIntent = explicitDeliveredSmsReceiver
                }
                deliveredIntent!!.putExtra("message_uri", messageUri?.toString() ?: "")
                deliveredIntent.putExtra(
                    "com.klinker.android.send_message.DELIVERED_SMS_BUNDLE",
                    deliveredParcelable
                )
                val deliveredPI = PendingIntent.getBroadcast(
                    context, messageId,
                    deliveredIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
                val sPI: ArrayList<PendingIntent?> = ArrayList<PendingIntent?>()
                val dPI: ArrayList<PendingIntent?> = ArrayList<PendingIntent?>()
                var body = text
                if (settings.stripUnicode) {
                    body = StripAccents.stripAccents(text)
                }
                if (settings.preText != "") {
                    body = settings.preText + " " + body
                }
                val smsManager = SmsManagerFactory.createSmsManager(settings)
                Log.v("send_transaction", "found sms manager")
                var j: Int
                if (settings.split) {
                    Log.v("send_transaction", "splitting message")
                    val splitData = SmsMessage.calculateLength(body, false)
                    j = (body.length + splitData[2]) / splitData[0]
                    Log.v("send_transaction", "length: $j")
                    var counter = false
                    if (settings.splitCounter && body.length > j) {
                        counter = true
                        j -= 6
                    }
                    val textToSend = splitByLength(body, j, counter)
                    for (p in textToSend.indices) {
                        val parts = smsManager.divideMessage(textToSend[p])
                        for (k in parts.indices) {
                            sPI.add(if (saveMessage) sentPI else null)
                            dPI.add(if (settings.deliveryReports && saveMessage) deliveredPI else null)
                        }
                        Log.v("send_transaction", "sending split message")
                        sendDelayedSms(
                            smsManager,
                            addresses[i], parts, sPI, dPI, delay, messageUri
                        )
                    }
                } else {
                    Log.v("send_transaction", "sending without splitting")
                    val parts = smsManager.divideMessage(body)
                    j = 0
                    while (j < parts.size) {
                        sPI.add(if (saveMessage) sentPI else null)
                        dPI.add(if (settings.deliveryReports && saveMessage) deliveredPI else null)
                        ++j
                    }
                    if (Utils.isDefaultSmsApp(context)) {
                        try {
                            Log.v("send_transaction", "sent message")
                            sendDelayedSms(
                                smsManager,
                                addresses[i], parts, sPI, dPI, delay, messageUri
                            )
                        } catch (var30: Exception) {
                            Log.v("send_transaction", "error sending message")
                            Log.e("Transaction", "exception thrown", var30)
                            try {
//                                ((Activity)this.context).getWindow().getDecorView().findViewById(16908290).post(new Runnable() {
//                                    public void run() {
//                                        Toast.makeText(Transaction.this.context, "Message could not be sent", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                            } catch (var29: Exception) {
                            }
                        }
                    } else {
                        smsManager.sendMultipartTextMessage(
                            addresses[i],
                            null as String?,
                            parts,
                            sPI,
                            dPI
                        )
                    }
                }
            }
        }
    }

    private fun sendDelayedSms(
        smsManager: SmsManager,
        address: String,
        parts: ArrayList<String>,
        sPI: ArrayList<PendingIntent?>,
        dPI: ArrayList<PendingIntent?>,
        delay: Int,
        messageUri: Uri?
    ) {
        Thread {
            try {
                Thread.sleep(delay.toLong())
            } catch (var3: Exception) {
            }
            if (checkIfMessageExistsAfterDelay(messageUri)) {
                Log.v("send_transaction", "message sent after delay")
                try {
                    smsManager.sendMultipartTextMessage(
                        address,
                        null as String?,
                        parts,
                        sPI,
                        dPI
                    )
                } catch (var2: Exception) {
                    Log.e("Transaction", "exception thrown", var2)
                }
            } else {
                Log.v(
                    "send_transaction",
                    "message not sent after delay, no longer exists"
                )
            }
        }.start()
    }

    private fun checkIfMessageExistsAfterDelay(messageUti: Uri?): Boolean {
        val query = context.contentResolver.query(
            messageUti!!,
            arrayOf("_id"),
            null as String?,
            null as Array<String?>?,
            null as String?
        )
        return if (query != null && query.moveToFirst()) {
            query.close()
            true
        } else {
            false
        }
    }

    private fun sendMmsMessage(
        text: String?,
        addresses: Array<String>,
        image: Array<Bitmap>,
        imageNames: Array<String>?,
        parts: List<Message.Part>?,
        subject: String
    ) {
        var address = ""
        for (i in addresses.indices) {
            address = address + addresses[i] + " "
        }
        address = address.trim { it <= ' ' }
        val data: ArrayList<MMSPart?> = ArrayList<MMSPart?>()
        var part: MMSPart
        for (i in image.indices) {
            val imageBytes = Message.bitmapToByteArray(image[i])
            part = MMSPart()
            part.MimeType = "image/jpeg"
            part.Name = imageNames?.get(i) ?: "image_" + System.currentTimeMillis()
            part.Data = imageBytes
            data.add(part)
        }
        var var16: Iterator<*>?
        if (parts != null) {
            var16 = parts.iterator()
            while (var16.hasNext()) {
                val p = var16.next()
                part = MMSPart()
                if (p.name != null) {
                    part.Name = p.name
                } else {
                    part.Name = p.contentType.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0]
                }
                part.MimeType = p.contentType
                part.Data = p.media
                data.add(part)
            }
        }
        if (text != null && text != "") {
            val mPart = MMSPart()
            mPart.Name = "text"
            mPart.MimeType = "text/plain"
            mPart.Data = text.toByteArray()
            data.add(mPart)
        }
        val info: MessageInfo
        if (Build.VERSION.SDK_INT <= 19) {
            var16 = null
            try {
                info = getBytes(context,
                    saveMessage, address.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray(), data.toTypedArray<MMSPart?>(), subject)
                val sender = MmsMessageSender(context, info.location, info.bytes.size.toLong())
                sender.sendMessage(info.token)
                val filter = IntentFilter()
                filter.addAction("com.android.mms.PROGRESS_STATUS")
                val receiver: BroadcastReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val progress = intent.getIntExtra("progress", -3)
                        Log.v(
                            "sending_mms_library",
                            "progress: $progress"
                        )
                        val progressIntent = Intent("com.klinker.android.send_message.MMS_PROGRESS")
                        progressIntent.putExtra("progress", progress)
                        BroadcastUtils.sendExplicitBroadcast(
                            context,
                            progressIntent,
                            "com.klinker.android.send_message.MMS_PROGRESS"
                        )
                        if (progress == 100) {
                            BroadcastUtils.sendExplicitBroadcast(
                                context,
                                Intent(),
                                "com.klinker.android.send_message.REFRESH"
                            )
                            try {
                                context.unregisterReceiver(this)
                            } catch (var6: Exception) {
                            }
                        } else if (progress == -2) {
                            Log.v("sending_mms_library", "sending aborted for some reason...")
                        }
                    }
                }
                context.registerReceiver(receiver, filter)
            } catch (var14: Throwable) {
                Log.e("Transaction", "exception thrown", var14)
            }
        } else {
            Log.v("Transaction", "using lollipop method for sending sms")
            if (settings.useSystemSending) {
                Log.v("Transaction", "using system method for sending")
                sendMmsThroughSystem(
                    context, subject, data, addresses,
                    explicitSentMmsReceiver
                )
            } else {
                try {
                    info = getBytes(context,
                        saveMessage, address.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray(), data.toTypedArray<MMSPart?>(), subject)
                    val requestManager = MmsRequestManager(context, info.bytes)
                    val request = SendRequest(
                        requestManager,
                        Utils.getDefaultSubscriptionId(),
                        info.location,
                        null as String?,
                        null as PendingIntent?,
                        null as String?,
                        null as Bundle?
                    )
                    val manager = MmsNetworkManager(context, Utils.getDefaultSubscriptionId())
                    request.execute(context, manager)
                } catch (var13: Exception) {
                    Log.e("Transaction", "error sending mms", var13)
                }
            }
        }
    }

    private fun splitByLength(s: String, chunkSize: Int, counter: Boolean): Array<String?> {
        val arraySize = Math.ceil(s.length.toDouble() / chunkSize.toDouble()).toInt()
        val returnArray = arrayOfNulls<String>(arraySize)
        var index = 0
        var i: Int
        i = 0
        while (i < s.length) {
            if (s.length - i < chunkSize) {
                returnArray[index++] = s.substring(i)
            } else {
                returnArray[index++] = s.substring(i, i + chunkSize)
            }
            i += chunkSize
        }
        if (counter && returnArray.size > 1) {
            i = 0
            while (i < returnArray.size) {
                returnArray[i] = "(" + (i + 1) + "/" + returnArray.size + ") " + returnArray[i]
                ++i
            }
        }
        return returnArray
    }

    fun checkMMS(message: Message): Boolean {
        return message.images.size != 0 || message.parts.size != 0 || settings.sendLongAsMms && Utils.getNumPages(
            settings, message.text
        ) > settings.sendLongAsMmsAfter || message.addresses.size > 1 && settings.group || message.subject != null
    }

    companion object {
        private const val TAG = "Transaction"
        lateinit var settings: Settings
        const val SENT_SMS_BUNDLE = "com.klinker.android.send_message.SENT_SMS_BUNDLE"
        const val DELIVERED_SMS_BUNDLE = "com.klinker.android.send_message.DELIVERED_SMS_BUNDLE"
        var NOTIFY_SMS_FAILURE = ".NOTIFY_SMS_FAILURE"
        const val MMS_ERROR = "com.klinker.android.send_message.MMS_ERROR"
        const val REFRESH = "com.klinker.android.send_message.REFRESH"
        const val MMS_PROGRESS = "com.klinker.android.send_message.MMS_PROGRESS"
        const val NOTIFY_OF_DELIVERY = "com.klinker.android.send_message.NOTIFY_DELIVERY"
        const val NOTIFY_OF_MMS = "com.klinker.android.messaging.NEW_MMS_DOWNLOADED"
        const val NO_THREAD_ID = 0L
        const val DEFAULT_EXPIRY_TIME = 604800L
        const val DEFAULT_PRIORITY = 129
        @Throws(MmsException::class)
        fun getBytes(
            context: Context,
            saveMessage: Boolean,
            recipients: Array<String?>,
            parts: Array<MMSPart?>?,
            subject: String?
        ): MessageInfo {
            val sendRequest = SendReq()
            for (i in recipients.indices) {
                val phoneNumbers = EncodedStringValue.extract(recipients[i])
                if (phoneNumbers != null && phoneNumbers.size > 0) {
                    sendRequest.addTo(phoneNumbers[0])
                }
            }
            if (subject != null) {
                sendRequest.subject = EncodedStringValue(subject)
            }
            sendRequest.date = Calendar.getInstance().timeInMillis / 1000L
            try {
                sendRequest.from = EncodedStringValue(Utils.getMyPhoneNumber(context))
            } catch (var18: Exception) {
                Log.e("Transaction", "error getting from address", var18)
            }
            val pduBody = PduBody()
            var size = 0L
            if (parts != null) {
                for (i in parts.indices) {
                    val part = parts[i]
                    if (part != null) {
                        try {
                            val partPdu = PduPart()
                            partPdu.name = part.Name.toByteArray()
                            partPdu.contentType = part.MimeType.toByteArray()
                            if (part.MimeType.startsWith("text")) {
                                partPdu.charset = 106
                            }
                            partPdu.contentLocation = part.Name.toByteArray()
                            val index = part.Name.lastIndexOf(".")
                            val contentId =
                                if (index == -1) part.Name else part.Name.substring(0, index)
                            partPdu.contentId = contentId.toByteArray()
                            partPdu.data = part.Data
                            pduBody.addPart(partPdu)
                            size += (2 * part.Name.toByteArray().size + part.MimeType.toByteArray().size + part.Data.size + contentId.toByteArray().size).toLong()
                        } catch (var17: Exception) {
                        }
                    }
                }
            }
            val out = ByteArrayOutputStream()
            SmilXmlSerializer.serialize(SmilHelper.createSmilDocument(pduBody), out)
            val smilPart = PduPart()
            smilPart.contentId = "smil".toByteArray()
            smilPart.contentLocation = "smil.xml".toByteArray()
            smilPart.contentType = "application/smil".toByteArray()
            smilPart.data = out.toByteArray()
            pduBody.addPart(0, smilPart)
            sendRequest.body = pduBody
            Log.v("Transaction", "setting message size to $size bytes")
            sendRequest.messageSize = size
            sendRequest.priority = 129
            sendRequest.deliveryReport = 129
            sendRequest.expiry = 604800000L
            sendRequest.messageClass = "personal".toByteArray()
            sendRequest.readReport = 129
            val composer = PduComposer(context, sendRequest)
            val bytesToSend: ByteArray
            bytesToSend = try {
                composer.make()
            } catch (var16: OutOfMemoryError) {
                throw MmsException("Out of memory!")
            }
            val info = MessageInfo()
            info.bytes = bytesToSend
            if (saveMessage) {
                try {
                    val persister = PduPersister.getPduPersister(context)
                    //info.location = persister.persist(sendRequest, Uri.parse("content://mms/outbox"), true, settings.getGroup(), (HashMap)null);
                } catch (var15: Exception) {
                    Log.v("sending_mms_library", "error saving mms message")
                    Log.e("Transaction", "exception thrown", var15)
                    insert(context, recipients, parts, subject)
                }
            }
            try {
                val query = context.contentResolver.query(
                    info.location!!,
                    arrayOf("thread_id"),
                    null as String?,
                    null as Array<String?>?,
                    null as String?
                )
                if (query != null && query.moveToFirst()) {
                    info.token = query.getLong(query.getColumnIndexOrThrow("thread_id"))
                    query.close()
                } else {
                    info.token = 4444L
                }
            } catch (var19: Exception) {
                Log.e("Transaction", "exception thrown", var19)
                info.token = 4444L
            }
            return info
        }

        private fun sendMmsThroughSystem(
            context: Context,
            subject: String,
            parts: List<MMSPart?>,
            addresses: Array<String>,
            explicitSentMmsReceiver: Intent?
        ) {
            try {
                val fileName = "send." + Math.abs(Random().nextLong()).toString() + ".dat"
                val mSendFile = File(context.cacheDir, fileName)
                val sendReq = buildPdu(context, addresses, subject, parts)
                val persister = PduPersister.getPduPersister(context)
                //Uri messageUri = persister.persist(sendReq, Uri.parse("content://mms/outbox"), true, settings.getGroup(), (HashMap)null);
                val intent: Intent
                if (explicitSentMmsReceiver == null) {
                    intent = Intent("com.klinker.android.messaging.MMS_SENT")
                    BroadcastUtils.addClassName(
                        context,
                        intent,
                        "com.klinker.android.messaging.MMS_SENT"
                    )
                } else {
                    intent = explicitSentMmsReceiver
                }

                //intent.putExtra("content_uri", messageUri.toString());
                intent.putExtra("file_path", mSendFile.path)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                val writerUri =
                    Uri.Builder().authority(context.packageName + ".MmsFileProvider").path(fileName)
                        .scheme("content").build()
                var writer: FileOutputStream? = null
                var contentUri: Uri? = null
                try {
                    writer = FileOutputStream(mSendFile)
                    writer.write(PduComposer(context, sendReq).make())
                    contentUri = writerUri
                } catch (var27: IOException) {
                    Log.e("Transaction", "Error writing send file", var27)
                } finally {
                    if (writer != null) {
                        try {
                            writer.close()
                        } catch (var25: IOException) {
                        }
                    }
                }
                val configOverrides = Bundle()
                configOverrides.putBoolean("enableGroupMms", settings.group)
                val httpParams = MmsConfig.getHttpParams()
                if (!TextUtils.isEmpty(httpParams)) {
                    configOverrides.putString("httpParams", httpParams)
                }
                configOverrides.putInt("maxMessageSize", MmsConfig.getMaxMessageSize())
                if (contentUri != null) {
                    SmsManagerFactory.createSmsManager(settings).sendMultimediaMessage(
                        context,
                        contentUri,
                        null as String?,
                        configOverrides,
                        pendingIntent
                    )
                } else {
                    Log.e("Transaction", "Error writing sending Mms")
                    try {
                        pendingIntent.send(5)
                    } catch (var26: PendingIntent.CanceledException) {
                        Log.e("Transaction", "Mms pending intent cancelled?", var26)
                    }
                }
            } catch (var29: Exception) {
                Log.e("Transaction", "error using system sending method", var29)
            }
        }

        private fun buildPdu(
            context: Context,
            recipients: Array<String>,
            subject: String,
            parts: List<MMSPart?>
        ): SendReq {
            val req = SendReq()
            val lineNumber = Utils.getMyPhoneNumber(context)
            if (!TextUtils.isEmpty(lineNumber)) {
                req.from = EncodedStringValue(lineNumber)
            }
            var size = recipients.size
            var i: Int
            i = 0
            while (i < size) {
                val recipient = recipients[i]
                req.addTo(EncodedStringValue(recipient))
                ++i
            }
            if (!TextUtils.isEmpty(subject)) {
                req.subject = EncodedStringValue(subject)
            }
            req.date = System.currentTimeMillis() / 1000L
            val body = PduBody()
            size = 0
            i = 0
            while (i < parts.size) {
                size += addTextPart(body, parts[i], i)
                ++i
            }
            val out = ByteArrayOutputStream()
            SmilXmlSerializer.serialize(SmilHelper.createSmilDocument(body), out)
            val smilPart = PduPart()
            smilPart.contentId = "smil".toByteArray()
            smilPart.contentLocation = "smil.xml".toByteArray()
            smilPart.contentType = "application/smil".toByteArray()
            smilPart.data = out.toByteArray()
            body.addPart(0, smilPart)
            req.body = body
            req.messageSize = size.toLong()
            req.messageClass = "personal".toByteArray()
            req.expiry = 604800L
            try {
                req.priority = 129
                req.deliveryReport = 129
                req.readReport = 129
            } catch (var11: InvalidHeaderValueException) {
            }
            return req
        }

        private fun addTextPart(pb: PduBody, p: MMSPart?, id: Int): Int {
            val filename = p!!.Name
            val part = PduPart()
            if (p.MimeType.startsWith("text")) {
                part.charset = 106
            }
            part.contentType = p.MimeType.toByteArray()
            part.contentLocation = filename.toByteArray()
            val index = filename.lastIndexOf(".")
            val contentId = if (index == -1) filename else filename.substring(0, index)
            part.contentId = contentId.toByteArray()
            part.data = p.Data
            pb.addPart(part)
            return part.data.size
        }

        private fun insert(
            context: Context,
            to: Array<String?>,
            parts: Array<MMSPart?>?,
            subject: String?
        ): Uri? {
            return try {
                val destUri = Uri.parse("content://mms")
                val recipients: MutableSet<String?> = HashSet<String?>()
                recipients.addAll(Arrays.asList(*to))
                val thread_id = Utils.getOrCreateThreadId(context, recipients)
                val dummyValues = ContentValues()
                dummyValues.put("thread_id", thread_id)
                dummyValues.put("body", " ")
                val dummySms =
                    context.contentResolver.insert(Uri.parse("content://sms/sent"), dummyValues)
                val now = System.currentTimeMillis()
                val mmsValues = ContentValues()
                mmsValues.put("thread_id", thread_id)
                mmsValues.put("date", now / 1000L)
                mmsValues.put("msg_box", 4)
                mmsValues.put("read", true)
                mmsValues.put("sub", subject ?: "")
                mmsValues.put("sub_cs", 106)
                mmsValues.put("ct_t", "application/vnd.wap.multipart.related")
                var imageBytes = 0L
                val var16 = parts!!.size
                for (var17 in 0 until var16) {
                    val part = parts[var17]
                    imageBytes += part!!.Data.size.toLong()
                }
                mmsValues.put("exp", imageBytes)
                mmsValues.put("m_cls", "personal")
                mmsValues.put("m_type", 128)
                mmsValues.put("v", 19)
                mmsValues.put("pri", 129)
                mmsValues.put("tr_id", "T" + java.lang.Long.toHexString(now))
                mmsValues.put("resp_st", 128)
                val res = context.contentResolver.insert(destUri, mmsValues)
                val messageId = res!!.lastPathSegment!!.trim { it <= ' ' }
                var var26 = parts.size
                var var19: Int
                var19 = 0
                while (var19 < var26) {
                    val part = parts[var19]
                    if (part!!.MimeType.startsWith("image")) {
                        createPartImage(context, messageId, part.Data, part.MimeType)
                    } else if (part.MimeType.startsWith("text")) {
                        createPartText(
                            context, messageId, String(
                                part.Data, charset("UTF-8")
                            )
                        )
                    }
                    ++var19
                }
                var26 = to.size
                var19 = 0
                while (var19 < var26) {
                    val addr = to[var19]
                    createAddr(context, messageId, addr)
                    ++var19
                }
                context.contentResolver.delete(dummySms!!, null as String?, null as Array<String?>?)
                res
            } catch (var21: Exception) {
                Log.v("sending_mms_library", "still an error saving... :(")
                Log.e("Transaction", "exception thrown", var21)
                null
            }
        }

        @Throws(Exception::class)
        private fun createPartImage(
            context: Context,
            id: String,
            imageBytes: ByteArray,
            mimeType: String
        ): Uri? {
            val mmsPartValue = ContentValues()
            mmsPartValue.put("mid", id)
            mmsPartValue.put("ct", mimeType)
            mmsPartValue.put("cid", "<" + System.currentTimeMillis() + ">")
            val partUri = Uri.parse("content://mms/$id/part")
            val res = context.contentResolver.insert(partUri, mmsPartValue)
            val os = context.contentResolver.openOutputStream(res!!)
            val `is` = ByteArrayInputStream(imageBytes)
            val buffer = ByteArray(256)
            val var10 = false
            var len: Int
            while (`is`.read(buffer).also { len = it } != -1) {
                os!!.write(buffer, 0, len)
            }
            os!!.close()
            `is`.close()
            return res
        }

        @Throws(Exception::class)
        private fun createPartText(
            context: Context,
            id: String,
            text: String
        ): Uri? {
            val mmsPartValue = ContentValues()
            mmsPartValue.put("mid", id)
            mmsPartValue.put("ct", "text/plain")
            mmsPartValue.put("cid", "<" + System.currentTimeMillis() + ">")
            mmsPartValue.put("text", text)
            val partUri = Uri.parse("content://mms/$id/part")
            return context.contentResolver.insert(partUri, mmsPartValue)
        }

        @Throws(Exception::class)
        private fun createAddr(
            context: Context,
            id: String,
            addr: String?
        ): Uri? {
            val addrValues = ContentValues()
            addrValues.put("address", addr)
            addrValues.put("charset", "106")
            addrValues.put("type", 151)
            val addrUri = Uri.parse("content://mms/$id/addr")
            return context.contentResolver.insert(addrUri, addrValues)
        }
    }
}

