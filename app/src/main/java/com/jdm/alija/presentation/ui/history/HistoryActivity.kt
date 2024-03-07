package com.jdm.alija.presentation.ui.history

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityHistoryBinding
import com.jdm.alija.domain.model.CallbackResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {
    private val historyAdapter by lazy { HistoryAdapter(this, this::onClickCallbackResult ) }
    private val historyViewModel: HistoryViewModel by viewModels()
    override val layoutResId: Int
        get() = R.layout.activity_history

    override fun initView() {
        binding.rvHistory.adapter = historyAdapter
    }

    override fun subscribe() {
        historyViewModel.callbackHistory.observe(this) {
            historyAdapter.submitList(it)
        }
    }

    override fun initEvent() {
        binding.appbarHistoryBack.setOnClickListener { finish() }
    }

    override fun initData() {
        historyViewModel.getCallbackResult()
    }
    private fun onClickCallbackResult(item: CallbackResult) {
        val smsUri = Uri.parse("smsto:" + item.mobile)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(smsUri)
        intent.putExtra("sms_body", "")
        goToActivity(intent)
    }
}