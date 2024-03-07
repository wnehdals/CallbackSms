package com.jdm.alija.presentation.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.jdm.alija.R
import com.jdm.alija.data.entity.BlackEntity
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.BlackRepository
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.domain.usecase.GetMobileUseCase
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.ui.msg.send.MessageSendActivity
import com.jdm.alija.presentation.util.Const.ACTION_SAVE_CONTACT
import com.jdm.alija.presentation.util.Const.ACTION_SEND_INCALL_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_SEND_OUTCALL_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_SEND_RELEASE_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_START_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_STOP_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.LOCATION_SERVICE_ID
import com.jdm.alija.presentation.util.FileUtil
import com.jdm.alija.presentation.util.SmsUtil
import com.klinker.android.send_message.Transaction
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import javax.inject.Inject

/*
class SmsService : Service() {
    lateinit var notificationManager: NotificationManagerCompat
    var step = 0
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var smsRepository: ContactRepository

    @Inject
    lateinit var smsUtil: SmsUtil
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}


 */
@AndroidEntryPoint
class SmsService : Service() {
    lateinit var notificationManager: NotificationManagerCompat
    var sendingMobile = ""
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var contactRepository: ContactRepository

    @Inject
    lateinit var blackListRepository: BlackRepository

    @Inject
    lateinit var groupRepository: GroupRepository
    @Inject
    lateinit var mobileUseCase: GetMobileUseCase

    @Inject
    lateinit var smsUtil: SmsUtil
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    fun caleanrToString(calendar: Calendar): String {
        return String.format(
            "%d%02d%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
    }

    suspend fun isValidDate(today: Int, selectGroup: Group): Boolean {
        when (today) {
            1 -> {
                return selectGroup.sun
            }

            2 -> {
                return selectGroup.mon
            }

            3 -> {
                return selectGroup.tue
            }

            4 -> {
                return selectGroup.wed
            }

            5 -> {
                return selectGroup.thu
            }

            6 -> {
                return selectGroup.fri
            }

            7 -> {
                return selectGroup.sat
            }

            else -> return false
        }
    }
    fun sendReleasecallMessage(context: Context, selectGroup: Group, mobile: String) {
        if (selectGroup.isBeforeCheck) {
            /*
            if (selectGroup.releaseCallImg.isEmpty() || selectGroup.releaseCallImg == "null") {
                val smsUri = Uri.parse("smsto:" + mobile)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setData(smsUri)
                intent.putExtra("sms_body", "${selectGroup.releaseCallText}")
                this@SmsService.startActivity(intent)
            } else {
                val intent = MessageSendActivity.getIntent(context, selectGroup.id, MessageSendActivity.RELEASE, mobile)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
            }
             */
            val intent = MessageSendActivity.getIntent(context, selectGroup.id, MessageSendActivity.RELEASE, mobile)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        } else {
            val text = selectGroup.releaseCallText
            val imgPath = selectGroup.releaseCallImg
            val smsUtil = SmsUtil()
            smsUtil.sendSms(context, mobile, text, imgPath)
        }
    }
    fun sendOutcallMessage(context: Context, selectGroup: Group, mobile: String) {
        if (selectGroup.isBeforeCheck) {
            /*
            if (selectGroup.outcallImg.isEmpty() || selectGroup.outcallImg == "null") {
                val smsUri = Uri.parse("smsto:" + mobile)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setData(smsUri)
                intent.putExtra("sms_body", "${selectGroup.outcallText}")
                this@SmsService.startActivity(intent)
            } else {

            }

             */
            val intent = MessageSendActivity.getIntent(context, selectGroup.id, MessageSendActivity.OUTCALL, mobile)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        } else {
            val text = selectGroup.outcallText
            val imgPath = selectGroup.outcallImg
            val smsUtil = SmsUtil()
            smsUtil.sendSms(context, mobile, text, imgPath)
        }
    }
    fun sendIncallMessage(context: Context, selectGroup: Group, mobile: String) {
        if (selectGroup.isBeforeCheck) {
            /*
            if (selectGroup.incallImg.isEmpty() || selectGroup.incallImg == "null") {
                val smsUri = Uri.parse("smsto:" + mobile)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setData(smsUri)
                intent.putExtra("sms_body", "${selectGroup.incallText}")
                this@SmsService.startActivity(intent)
            } else {
                val intent = MessageSendActivity.getIntent(context, selectGroup.id, MessageSendActivity.INCALL, mobile)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
            }
             */
            val intent = MessageSendActivity.getIntent(context, selectGroup.id, MessageSendActivity.INCALL, mobile)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
        } else {
            val text = selectGroup.incallText
            val imgPath = selectGroup.incallImg
            val smsUtil = SmsUtil()
            smsUtil.sendSms(context, mobile, text, imgPath)
        }
    }

    suspend fun isValideDuplicate(contactEntity: ContactEntity?, duplicateIdx: Int): Boolean {
        val calendar = Calendar.getInstance()
        if (contactEntity == null) {
            return true
        } else {
            val latestCal = Calendar.getInstance()
            latestCal.set(contactEntity.year, contactEntity.month, contactEntity.day)
            if (duplicateIdx == 0) {
                if (caleanrToString(latestCal) == caleanrToString(calendar)) {
                    return false
                } else {
                    return true
                }
            } else if (duplicateIdx == 1) {
                latestCal.add(Calendar.DATE, 7)
                if (calendar.compareTo(latestCal) == 1) {
                    //send
                    return true
                } else {
                    return false
                }
            } else {
                latestCal.add(Calendar.MONTH, 1)
                if (calendar.compareTo(latestCal) == 1) {
                    //send
                    return true
                } else {
                    return false
                }
            }
        }
    }

    suspend fun findGroupByMobile(groupList: List<Group>, mobile: String): Group? {
        var selectGroup: Group? = null
        for (group in groupList) {
            if (selectGroup != null) {
                break
            }
            for (contact in group.contactList) {
                var contactMobile = contact.mobile.replace("-", "").trim()
                if (mobile == contactMobile) {
                    selectGroup = group
                    break
                }
            }
        }
        return selectGroup
    }
    fun isBlackList(blackList: List<BlackContact>, mobile: String): Boolean {
        var flag = false
        for (black in blackList) {
            var blockMobile = black.mobile.replace("-", "").trim()
            if (blockMobile == mobile) {
                flag = true
                break
            }
        }
        return flag
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationManager = NotificationManagerCompat.from(applicationContext)
        val action = intent?.action
        Log.e("onstartcommand", action.toString())
        when (action) {
            ACTION_START_LOCATION_SERVICE -> {
                startLocationUpdates()
            }

            ACTION_STOP_LOCATION_SERVICE -> {
                stopLocationUpdates()
            }
            ACTION_SEND_RELEASE_SERVICE -> {
                var mobile = intent.getStringExtra("mobile") ?: ""
                mobile = mobile.replace("-", "").trim()

                scope.launch {
                    if (mobile.isEmpty()) {
                        return@launch
                    } else {
                        sendingMobile = mobile
                    }
                    /*
                    if (mobile.length < 3 && mobile.substring(0,3) != "010") {
                        return@launch
                    }

                     */
                    var blackList = blackListRepository.getAllContact()
                    if (isBlackList(blackList, mobile)) {
                        return@launch
                    }


                    var groupList = groupRepository.getAllGroup()
                    var selectGroup: Group? = findGroupByMobile(groupList, mobile)
                    // default
                    if (selectGroup == null) {
                        selectGroup = groupRepository.getGroup(GroupEntity.DEFAULT_GROUP_ID)
                        if (!selectGroup!!.isReleaseCallActive) {
                            return@launch
                        }
                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendReleasecallMessage(this@SmsService, selectGroup!!, mobile)
                            }
                        }

                    } else {
                        if (!selectGroup!!.isIncallActive) {
                            return@launch
                        }

                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendReleasecallMessage(this@SmsService, selectGroup!!, mobile)
                            }
                        }

                    }


                }
            }
            ACTION_SEND_OUTCALL_SERVICE -> {
                var mobile = intent.getStringExtra("mobile") ?: ""
                mobile = mobile.replace("-", "").trim()
                scope.launch {
                    if (mobile.isEmpty()) {
                        return@launch
                    } else {
                        sendingMobile = mobile
                    }
                    var blackList = blackListRepository.getAllContact()
                    if (isBlackList(blackList, mobile)) {
                        return@launch
                    }

                    var groupList = groupRepository.getAllGroup()
                    var selectGroup: Group? = findGroupByMobile(groupList, mobile)
                    // default
                    if (selectGroup == null) {
                        selectGroup = groupRepository.getGroup(GroupEntity.DEFAULT_GROUP_ID)
                        if (!selectGroup!!.isOutcallActivie) {
                            return@launch
                        }
                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendOutcallMessage(this@SmsService, selectGroup!!, mobile)
                            }
                        }

                    } else {
                        if (!selectGroup!!.isIncallActive) {
                            return@launch
                        }

                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendOutcallMessage(this@SmsService, selectGroup!!, mobile)

                            }
                        }

                    }


                }
            }

            ACTION_SEND_INCALL_SERVICE -> {
                var mobile = intent.getStringExtra("mobile") ?: ""
                mobile = mobile.replace("-", "").trim()
                scope.launch {
                    if (mobile.isEmpty()) {
                        return@launch
                    } else {
                        sendingMobile = mobile
                    }
                    var blackList = blackListRepository.getAllContact()
                    if (isBlackList(blackList, mobile)) {
                        return@launch
                    }

                    var groupList = groupRepository.getAllGroup()
                    var selectGroup: Group? = findGroupByMobile(groupList, mobile)
                    // default
                    if (selectGroup == null) {
                        selectGroup = groupRepository.getGroup(GroupEntity.DEFAULT_GROUP_ID)
                        if (!selectGroup!!.isIncallActive) {
                            return@launch
                        }
                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendIncallMessage(this@SmsService, selectGroup!!, mobile)
                            }
                        }

                    } else {
                        if (!selectGroup!!.isIncallActive) {
                            return@launch
                        }

                        var calendar = Calendar.getInstance()
                        var today = calendar.get(Calendar.DAY_OF_WEEK)
                        if (isValidDate(today, selectGroup!!)) {
                            val contactEntity = contactRepository.selectContactByMobile(mobile)
                            if (isValideDuplicate(contactEntity, selectGroup!!.dupicateIdx)) {
                                sendIncallMessage(this@SmsService, selectGroup!!, mobile)
                            }
                        }

                    }


                }
            }
            ACTION_SAVE_CONTACT -> {
                scope.launch {
                    var calendar = Calendar.getInstance()
                    if (!sendingMobile.isNullOrEmpty()) {
                        val contactEntity = ContactEntity(
                            sendingMobile,
                            sendingMobile,
                            "",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DATE),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE)
                        )
                        contactRepository.insert(contactEntity)
                        var name = ""
                        val contacts = mobileUseCase.invoke()
                        for(contact in contacts) {
                            if (contact.mobile.replace("-","").trim() == sendingMobile) {
                                name = contact.name
                                break
                            }
                        }
                        var body = if (name.isEmpty()) sendingMobile else name
                        val smsUri = Uri.parse("smsto:" + sendingMobile)
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.setData(smsUri)
                        intent.putExtra("sms_body", "")
                        var notification = getNotification(
                            title = "알리자",
                            body = "${body}에게 메시지 발송완료했습니다.",
                            channelId = "알리자",
                            channelName = "전송 알림",
                            intent = intent,
                            setOnGoing = false,
                            pushNotiId = 1
                        )
                        if (ActivityCompat.checkSelfPermission(
                                this@SmsService,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            notificationManager.notify(1, notification)
                        }

                        sendingMobile = ""
                    }
                }
            }
        }


        return START_REDELIVER_INTENT
        //return super.onStartCommand(intent, flags, startId)
    }

    fun getNotification(
        title: String,
        body: String,
        channelId: String,
        channelName: String,
        pushNotiId: Int,
        intent: Intent,
        setOnGoing: Boolean = false
    ): Notification {
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                var channel =
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(applicationContext, channelId)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
        }
        builder
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(
                R.drawable.ic_noti
            )
            .setColor(ContextCompat.getColor(applicationContext, R.color.blue_400))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(setOnGoing)
            .setAutoCancel(false)

        return builder.build()
    }

    fun startLocationUpdates() {
        var title = "알리자"
        var body = "알리자 서비스를 중지하려면 탭하여 OFF시켜주세요."
        var channelId = "알리자"
        val intent = Intent(this, MainActivity::class.java)
        startForeground(
            LOCATION_SERVICE_ID,
            getNotification(
                title = title,
                body = body,
                channelId = channelId,
                channelName = "알림",
                pushNotiId = 0,
                intent = intent,
                setOnGoing = true
            )
        )
    }

    fun stopLocationUpdates() {
        job.cancel()
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()

    }
}
