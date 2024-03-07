package com.jdm.alija.presentation.ui.msg.send

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityMessageSendBinding
import com.jdm.alija.presentation.util.SmsUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageSendActivity : BaseActivity<ActivityMessageSendBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_message_send
    private val messageSendViewModel: MessageSendViewModel by viewModels()
    private val smsUtil = SmsUtil()
    private var type: Int = -1
    private var mobile = ""
    override fun initView() {
    }

    override fun subscribe() {
        messageSendViewModel.text.observe(this) {
            binding.etMessageSend.setText(it)
        }
        messageSendViewModel.imgPath.observe(this) {
            Glide.with(this)
                .load(it)
                .error(R.drawable.ic_img_fail_black)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivMeessageSendPhoto)
        }
    }

    override fun initEvent() {
        binding.btMessageSend.setOnClickListener {
            if (mobile.isEmpty())
                return@setOnClickListener
            when (type) {
                MessageSendActivity.INCALL -> {
                    smsUtil.sendSms(this, mobile, messageSendViewModel.group.incallText, messageSendViewModel.group.incallImg)
                }
                MessageSendActivity.OUTCALL -> {
                    smsUtil.sendSms(this, mobile, messageSendViewModel.group.outcallText, messageSendViewModel.group.outcallImg)
                }
                MessageSendActivity.RELEASE -> {
                    smsUtil.sendSms(this, mobile, messageSendViewModel.group.releaseCallText, messageSendViewModel.group.releaseCallImg)
                }
            }
            finish()

        }
    }

    override fun initData() {
        type = intent.getIntExtra(TYPE, -1)
        val groupId = intent.getIntExtra(GROUP_ID, -1)
        mobile = intent.getStringExtra(MOBILE)?: ""
        messageSendViewModel.getGroup(groupId, type)
    }
    companion object {
        val GROUP_ID = "GROUP_ID"
        val TYPE = "TYPE"
        val MOBILE = "MOBILE"
        val INCALL = 0
        val OUTCALL = 1
        val RELEASE = 2
        fun getIntent(context: Context, groupId: Int, type: Int, mobile: String) : Intent{
            val intent = Intent(context, MessageSendActivity::class.java)
            intent.putExtra(GROUP_ID, groupId)
            intent.putExtra(TYPE, type)
            intent.putExtra(MOBILE, mobile)
            return intent
        }
    }
}