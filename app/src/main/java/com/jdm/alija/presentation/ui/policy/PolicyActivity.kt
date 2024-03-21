package com.jdm.alija.presentation.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityPolicyBinding

class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_policy

    override fun initView() {
    }

    override fun subscribe() {
    }

    override fun initEvent() {
        binding.llPolicyTopSkt.setOnClickListener {
            clearVisibility()
            binding.llPolicySkt.visibility = View.VISIBLE
            binding.tvPolicyTopSkt.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_400))
            binding.tvPolicySkt.isSelected = true
        }
        binding.llPolicyTopKt.setOnClickListener {
            clearVisibility()
            binding.llPolicyKt.visibility = View.VISIBLE
            binding.tvPolicyTopKt.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_400))
            binding.tvPolicyKt.isSelected = true
        }
        binding.llPolicyTopLg.setOnClickListener {
            clearVisibility()
            binding.llPolicyLg.visibility = View.VISIBLE
            binding.tvPolicyTopLg.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_400))
            binding.tvPolicyLg.isSelected = true
        }
        binding.ivPolicy.setOnClickListener {
            finish()
        }
    }
    private fun clearVisibility() {
        binding.llPolicySkt.visibility = View.GONE
        binding.llPolicyKt.visibility = View.GONE
        binding.llPolicyLg.visibility = View.GONE
        binding.tvPolicyTopSkt.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_100))
        binding.tvPolicyTopKt.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_100))
        binding.tvPolicyTopLg.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_100))
        binding.tvPolicySkt.isSelected = false
        binding.tvPolicyKt.isSelected = false
        binding.tvPolicyLg.isSelected = false
    }

    override fun initData() {
    }
    companion object {
        fun getIntent(context: Context) : Intent{
            return Intent(context, PolicyActivity::class.java)

        }
    }
}