package com.jdm.alija.presentation.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.telecom.Call
import android.util.TypedValue
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jdm.alija.R
import com.jdm.alija.domain.model.CallType

//*************************************************************************************************************************************************
// Context, Activity, Fragment
//


fun AppCompatActivity.requestMultiplePermission(
    permissions: Array<String>,
    passAction: () -> Unit,
    failAction: () -> Unit
) {
    val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var permissionFlag = true
        for (entry in it.entries) {
            if (!entry.value) {
                permissionFlag = false
                break
            }
        }
        if (!permissionFlag) {
            failAction()
        } else {
            passAction()
        }
    }
    launcher.launch(permissions)
}

fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    resources.displayMetrics
).toInt()

fun Activity.slideLeft() {
    overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
}

fun Activity.slideRight() {
    overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
}

fun Activity.slideUp() {
    overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit)
}

fun Activity.slideDown() {
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit)
}
val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Call.toCallType() = CallType(
    status = state.toCallStatus(),
    displayName = details.handle.schemeSpecificPart,
)
fun Int.toCallStatus() = when (this) {
    Call.STATE_ACTIVE -> CallType.Status.ACTIVE
    Call.STATE_RINGING -> CallType.Status.RINGING
    Call.STATE_CONNECTING -> CallType.Status.CONNECTING
    Call.STATE_DIALING -> CallType.Status.DIALING
    Call.STATE_DISCONNECTED -> CallType.Status.DISCONNECTED
    else -> CallType.Status.UNKNOWN
}