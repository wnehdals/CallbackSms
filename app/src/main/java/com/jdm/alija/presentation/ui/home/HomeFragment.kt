package com.jdm.alija.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.jdm.alija.R
import com.jdm.alija.SmsService
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentHomeBinding
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.util.Const.ACTION_START_LOCATION_SERVICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_home
    private val mainViewModel : MainViewModel by activityViewModels()
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

    }

    override fun initEvent() {
        binding.on.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickStartService)
        }
        binding.off.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickStopService)
        }
    }

    override fun subscribe() {
    }

    override fun initData() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
        val TAG = "HomeFragment"
    }
}