package com.jdm.alija.presentation.ui.home

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.data.entity.ContactEntity
import com.jdm.alija.databinding.FragmentHomeBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.dialog.LoadingDialog
import com.jdm.alija.domain.model.Advertise
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.MapRepository
import com.jdm.alija.presentation.service.SmsService
import com.jdm.alija.presentation.ui.advertise.AdvertiseActivity
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.ui.msg.AdvertisePagerAdapte
import com.jdm.alija.presentation.util.Const
import com.jdm.alija.presentation.util.Const.SERVICE_NAME
import com.jdm.alija.presentation.util.PreferenceHelper
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_home
    private val mainViewModel : MainViewModel by activityViewModels()
    private val loadingDialog = LoadingDialog()
    @Inject
    lateinit var contactRepository: ContactRepository
    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    private val adList = listOf<Advertise>(Advertise( "https://hizonenews.com/go/1.jpg"), Advertise("https://hizonenews.com/go/2.jpg"), Advertise("https://hizonenews.com/go/3.jpg"))
    private var bannerPosition = 0
    lateinit var job : Job
    private val advertisePagerAdapter : AdvertisePagerAdapte by lazy {
        AdvertisePagerAdapte(requireContext(), adList, this::onClickAd)
    }
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
    private fun onClickAd(item: Advertise) {
        (requireActivity() as MainActivity).goToActivity(AdvertiseActivity.getIntent(requireContext()))
    }
    override fun initView() {
        showLoading()
        if (!preferenceHelper.getStopGuide()) {
            showUsageGuideDialog()
        }
        binding.vpMessage.adapter = advertisePagerAdapter
        binding.vpMessage.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        bannerPosition = Int.MAX_VALUE / 2 - Math.ceil(adList.size.toDouble() / 2).toInt()
        binding.vpMessage.setCurrentItem(bannerPosition, false)
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
        binding.vpMessage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bannerPosition = position
                //binding.tvMessageAdIndicator.text = getString(R.string.viewpager2_banner, (bannerPosition % adList.size)+1, adList.size)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_IDLE ->{
                        if (!job.isActive) scrollJobCreate()
                    }

                    ViewPager2.SCROLL_STATE_DRAGGING -> {}

                    ViewPager2.SCROLL_STATE_SETTLING -> {}
                }
            }
        })

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
    fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {
            delay(3000)
            binding.vpMessage.setCurrentItem(++bannerPosition, true)
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

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
        val TAG = "HomeFragment"
    }
}
/*
mainViewModel.deleteContact()
            lifecycleScope.launch {
                val placeName = URLEncoder.encode("웃는바른이치과교정과치과의원", "UTF-8")
                mapRepository.getLocation("서울 서초구 서초대로 13 방배 마에스트로 제1동 2층") {
                    val routeUrl = "nmap://route/public?dlat=${it.lat}&dlng=${it.long}&dname=${placeName}&appname=com.jdm.alija"
                    val url = "nmap://search?query=${placeName}&appname=com.jdm.alija"

                    Log.e("url", "${routeUrl}")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(routeUrl))
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    val list = requireContext().packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    if (list == null || list.isEmpty()) {
                        requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.nmap")))
                    } else {
                        requireContext().startActivity(intent)
                    }
                }
            }

 */