package com.jdm.alija.presentation.ui.home

import android.app.ActivityManager
import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentHomeBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.util.Const.SERVICE_NAME
import com.jdm.alija.presentation.util.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_home
    private val mainViewModel : MainViewModel by activityViewModels()
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
        if (!preferenceHelper.getStopGuide()) {
            showUsageGuideDialog()
        }
        setActiveUI(isSmsServiceRunning())
    }
    private fun setActiveUI(isActive: Boolean) {
        if (isActive) {
            binding.lvHome.playAnimation()
            binding.tvHome.text = getString(R.string.str_on)
        } else {
            binding.lvHome.cancelAnimation()
            binding.tvHome.text = getString(R.string.str_off)
        }
    }

    override fun initEvent() {
        binding.lvHome.setOnClickListener {
            setActiveUI(!isSmsServiceRunning())
            if (isSmsServiceRunning()) {
                mainViewModel.setEvent(MainContract.MainEvent.OnClickStopService)
            } else {
                mainViewModel.setEvent(MainContract.MainEvent.OnClickStartService)
            }
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
    }

    override fun initData() {
    }
    private fun isSmsServiceRunning(): Boolean {
        val activityManager: ActivityManager = requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(Int.MAX_VALUE).forEach {
            if (it.service.className == SERVICE_NAME) {
                if (it.foreground) {
                    return true
                }
            } else {
                return false
            }
        }
        return false
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