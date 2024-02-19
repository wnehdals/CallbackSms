package com.jdm.alija

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.jdm.alija.domain.repository.SmsRepository
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.util.Const.ACTION_START_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_STOP_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.LOCATION_SERVICE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SmsService : Service() {
    lateinit var notificationManager: NotificationManagerCompat
    var step = 0
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    @Inject
    lateinit var smsRepository: SmsRepository
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = NotificationManagerCompat.from(applicationContext)
        val action = intent?.action
        if (action != null) {
            if (action == ACTION_START_LOCATION_SERVICE) {
                startLocationUpdates()
            } else if (action === ACTION_STOP_LOCATION_SERVICE){
                stopLocationUpdates()
            } else {
                //val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                //pendingIntent.send()
                var mobile = intent.getStringExtra("mobile")?: ""
                mobile = mobile.replace("-","")
                scope.launch {
                    var list = smsRepository.getAllSms()
                    list = list.filter { (it.mobile.replace("-","")) == mobile }
                    Log.e("service", list.toString())
                    for(i in list) {
                        val smsUri = Uri.parse("smsto:" + i.mobile)
                        val intent = Intent(Intent.ACTION_SENDTO)
                        if (i.imgUri.isEmpty() || i.imgUri == "null") {

                        } else {
                            intent.setType("image/*")
                            val file = File(i.imgUri.toString())
                            val photoUri = FileProvider.getUriForFile(
                                applicationContext,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                file
                            )
                            intent.putExtra(Intent.EXTRA_STREAM, photoUri)

                            Log.e("service", "${i.imgUri}")
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.setData(smsUri)
                        intent.putExtra("sms_body", "${i.text}")
                        this@SmsService.startActivity(intent)
                    }
                }


            }
        }
        return START_STICKY
        //return super.onStartCommand(intent, flags, startId)
    }
    fun getNotification(title: String, body: String, channelId: String, pushNotiId: Int, intent: Intent, setOnGoing: Boolean = false): Notification {
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                var channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
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
                R.drawable.ic_launcher_foreground
            )
            .setColor(ContextCompat.getColor(applicationContext, R.color.green_200))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(setOnGoing)
            .setAutoCancel(false)

        return builder.build()
    }
    fun startLocationUpdates() {
        var title = "위치 알람"
        var body = "위치 알람을 중지하려면 탭하여 알람을 OFF시켜주세요."
        var channelId = "위치 알람"
        val intent = Intent(this, MainActivity::class.java)
        startForeground(LOCATION_SERVICE_ID, getNotification(title = title, body = body, channelId = channelId, pushNotiId = 0, intent = intent, setOnGoing = true))
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