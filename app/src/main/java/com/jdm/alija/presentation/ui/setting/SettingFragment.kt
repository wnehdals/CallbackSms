package com.jdm.alija.presentation.ui.setting

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentSettingBinding
import com.jdm.alija.presentation.ui.blacklist.BlackListActivity
import com.jdm.alija.presentation.ui.group.GroupActivity
import com.jdm.alija.presentation.ui.history.HistoryActivity
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.ui.msg.set.MessageActivity
import com.jdm.alija.presentation.ui.policy.PolicyActivity
import com.jdm.alija.presentation.util.slideLeft
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_setting
    private val mainViewModel : MainViewModel by activityViewModels()
    override var onBackPressedCallback: OnBackPressedCallback? = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickHomeButton)
        }
    }

    override fun initView() {
    }

    override fun initEvent() {
        binding.llCallbackSetting.setOnClickListener {
            (requireActivity() as MainActivity).goToActivity(MessageActivity.getIntent(requireContext(), "CallBack"))
        }
        binding.llCallbackSettingTelephone.setOnClickListener {
            (requireActivity() as MainActivity).goToActivity(PolicyActivity.getIntent(requireContext()))
        }
        binding.llSettingGroup.setOnClickListener {
            (requireActivity() as MainActivity).goToActivity(GroupActivity.getIntent(requireContext()))
        }
        binding.llSettingBlacklist.setOnClickListener {
            (requireActivity() as MainActivity).goToActivity(BlackListActivity.getIntent(requireContext()))
        }
        binding.llSettingHistory.setOnClickListener {
            (requireActivity() as MainActivity).goToActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
    }

    override fun subscribe() {
    }

    override fun initData() {
    }

    companion object {
        val TAG = "SettingFragment"
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}