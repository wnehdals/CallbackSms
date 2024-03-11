package com.jdm.alija.presentation.ui.home

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentHomeBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.dialog.LoadingDialog
import com.jdm.alija.presentation.service.SmsService
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.util.Const
import com.jdm.alija.presentation.util.Const.SERVICE_NAME
import com.jdm.alija.presentation.util.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_home
    private val mainViewModel : MainViewModel by activityViewModels()
    private val loadingDialog = LoadingDialog()
    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    override var onBackPressedCallback: OnBackPressedCallback? =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    exitApp()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.str_app_finish_guide_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        }

    override fun initView() {
        showLoading()

        if (!preferenceHelper.getStopGuide()) {
            showUsageGuideDialog()
        }
        Glide.with(requireContext())
            .load("https://hizonenews.com/go/1.jpg")
            .into(binding.ivHomeAd)
        //setActiveUI(isSmsServiceRunning())
    }
    private fun setActiveUI(isActive: Boolean) {
        if (isActive) {
            if (!binding.lvHome.isAnimating)
                binding.lvHome.playAnimation()
            binding.tvHome.text = getString(R.string.str_on)
            binding.tvHomeAvtive.text = getString(R.string.str_on_guide)
        } else {
            if (binding.lvHome.isAnimating)
                binding.lvHome.cancelAnimation()
            binding.tvHome.text = getString(R.string.str_off)
            binding.tvHomeAvtive.text = getString(R.string.str_off_guide)
        }
    }

    override fun initEvent() {
        binding.lvHome.setOnClickListener {
            //setActiveUI(!isSmsServiceRunning())
            showLoading()
            if (Const.isSmsServiceRunning(requireContext())) {
                mainViewModel.setEvent(MainContract.MainEvent.OnClickStopService)
            } else {
                mainViewModel.setEvent(MainContract.MainEvent.OnClickStartService)
            }

        }
        binding.llHomeGuide.setOnClickListener {
            mainViewModel.deleteContact()
            //mainViewModel.setEvent(MainContract.MainEvent.OnClickStopService)
        }
        /*
        binding.on.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickStartService)
        }
        binding.off.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickStopService)
        }

         */

    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(2000L)
                    dismissLoading()
                    setActiveUI(Const.isSmsServiceRunning(requireContext()))
                }
            }
        }
    }
    private fun showLoading() {
        val fm = parentFragmentManager.findFragmentByTag(LoadingDialog.TAG)
        if (fm == null) {
            loadingDialog.show(parentFragmentManager, LoadingDialog.TAG)
        }
    }
    private fun dismissLoading() {
        val fm = parentFragmentManager.findFragmentByTag(LoadingDialog.TAG)
        fm?.let {
            loadingDialog.dismiss()
        }

    }

    override fun initData() {
    }

    private fun showUsageGuideDialog() {
        CommonDialog(
            title = getString(R.string.str_usage_guide),
            msg = getString(R.string.str_usage_guide_1),
            leftText = getString(R.string.str_stop_see),
            rightText = getString(R.string.str_confirm),
            leftClick = {
                preferenceHelper.setStopGuide(true)
            }, rightClick = {
                preferenceHelper.setStopGuide(false)
            }, isCancel = false
        ).show(parentFragmentManager, CommonDialog.TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
        val TAG = "HomeFragment"
    }
}