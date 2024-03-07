package com.jdm.alija.presentation.ui.advertise

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityAdvertiseBinding

class AdvertiseActivity : BaseActivity<ActivityAdvertiseBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_advertise

    override fun initView() {
    }

    override fun subscribe() {
    }

    override fun initEvent() {
        binding.ivAppbarAdvertiseBack.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
    }
    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, AdvertiseActivity::class.java)
            return intent
        }
    }
}