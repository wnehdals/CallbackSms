package com.jdm.alija

import android.view.View
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityCallBinding
import com.jdm.alija.domain.model.CallType
import com.jdm.alija.presentation.util.CallManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class CallActivity : BaseActivity<ActivityCallBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_call
    private var updateDisposable = CompositeDisposable()
    private var timerDisposable = CompositeDisposable()

    override fun initView() {
        hideBottomNavigationBar()

    }

    override fun subscribe() {
    }

    override fun initEvent() {
        binding.callReceive.setOnClickListener {
            CallManager.acceptCall()
        }
        binding.callReject.setOnClickListener {
            CallManager.cancelCall()
        }
    }

    override fun initData() {
    }
    private fun hideBottomNavigationBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onResume() {
        super.onResume()
        /*
        CallManager.update()
            .doOnEach { Log.i(TAG, "update call : ${it}")  }
            .doOnError { throwable -> Log.e(TAG, "Error processing call") }
            .subscribe { updateView(it) }
            .addTo(updateDisposable)

         */
    }
    private fun updateView(callType: CallType) {
        binding.callS.visibility = when (callType.status) {
            CallType.Status.ACTIVE -> View.GONE
            else                  -> View.VISIBLE
        }
        binding.callS.text = when (callType.status) {
            CallType.Status.CONNECTING   -> "Connecting…"
            CallType.Status.DIALING      -> "Calling…"
            CallType.Status.RINGING      -> "Incoming call"
            CallType.Status.ACTIVE       -> ""
            CallType.Status.DISCONNECTED -> "Finished call"
            CallType.Status.UNKNOWN      -> ""
        }
        binding.callDuration.visibility = when (callType.status) {
            CallType.Status.ACTIVE -> View.VISIBLE
            else                  -> View.GONE
        }
        binding.callReject.visibility = when (callType.status) {
            CallType.Status.DISCONNECTED -> View.GONE
            else                        -> View.VISIBLE
        }

        if (callType.status == CallType.Status.DISCONNECTED) {
            binding.callReject.postDelayed({ finish() }, 3000)
        }

        when (callType.status) {
            CallType.Status.ACTIVE       -> startTimer()
            CallType.Status.DISCONNECTED -> stopTimer()
            else                        -> Unit
        }

        binding.callName.text = callType.displayName ?: "Unknown"

        binding.callReceive.visibility = when (callType.status) {
            CallType.Status.RINGING -> View.VISIBLE
            else                   -> View.GONE
        }
    }
    private fun startTimer() {
        Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { binding.callDuration.text = it.toDurationString() }
            .addTo(timerDisposable)
    }

    private fun stopTimer() {
        timerDisposable.dispose()
    }

    override fun onDestroy() {
        if (!updateDisposable.isDisposed) {
            updateDisposable.dispose()
        }
        if (!timerDisposable.isDisposed) {
            timerDisposable.dispose()
        }
        super.onDestroy()
    }
    private fun Long.toDurationString() = String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))
    companion object {
        val TAG = this.javaClass.simpleName
    }
}