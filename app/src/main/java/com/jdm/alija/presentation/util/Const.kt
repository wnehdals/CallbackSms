package com.jdm.alija.presentation.util

import android.app.ActivityManager
import android.content.Context
import android.util.Log

object Const {
    const val ACTION_START_LOCATION_SERVICE = "ACTION_START_LOCATION_SERVICE"
    const val ACTION_STOP_LOCATION_SERVICE = "ACTION_STOP_LOCATION_SERVICE"
    const val ACTION_SEND_INCALL_SERVICE = "ACTION_SEND_INCALL_SERVICE"
    const val ACTION_SEND_OUTCALL_SERVICE = "ACTION_SEND_OUTCALL_SERVICE"
    const val ACTION_SEND_RELEASE_SERVICE = "ACTION_SEND_RELEASE_SERVICE"
    const val ACTION_SAVE_CONTACT = "ACTION_SAVE_CONTACT"
    const val ACTION_SEND_SMS_SERVICE = "ACTION_SEND_SMS_SERVICE"
    const val LOCATION_SERVICE_ID = 100
    const val SERVICE_NAME = "com.jdm.alija.presentation.service.SmsService"
    const val BUNDLE_KEY_ALARM = "alarm"
    const val MAX_INQUIRY_UPLOAD_FILE_SIZE = 10 * 1024 * 1024

    fun isSmsServiceRunning(context: Context): Boolean {
        var flag = false
        val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for(serivceInfo in activityManager.getRunningServices(Int.MAX_VALUE)) {
            Log.e("homefragment", serivceInfo.service.className)
            if (serivceInfo.service.className == SERVICE_NAME) {
                flag = true
                break
            }
        }
        return flag
    }
}