package com.jdm.alija.presentation.ui.msg

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentMessageBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.domain.model.Advertise
import com.jdm.alija.presentation.ui.advertise.AdvertiseActivity
import com.jdm.alija.presentation.ui.group.GroupActivity
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.util.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment : BaseFragment<FragmentMessageBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_message
    private val mainViewModel : MainViewModel by activityViewModels()
    private val messageViewModel: MessageViewModel by viewModels()
    @Inject
    lateinit var preferenceHelper: PreferenceHelper
    private var bannerPosition = 0
    override var onBackPressedCallback: OnBackPressedCallback? = object : OnBackPressedCallback(true) {
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
    lateinit var job : Job
    private val adList = listOf<Advertise>()
    private val advertisePagerAdapter : AdvertisePagerAdapte by lazy {
        AdvertisePagerAdapte(requireContext(), adList, this::onClickAd)
    }
    override fun initView() {
        if (!preferenceHelper.getStopGuide()) {
            showUsageGuideDialog()
        }
        binding.vpMessage.adapter = advertisePagerAdapter
        binding.vpMessage.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.tvMessageAdIndicator.text = getString(R.string.viewpager2_banner, 1, adList.size)
        bannerPosition = Int.MAX_VALUE / 2 - Math.ceil(adList.size.toDouble() / 2).toInt()

        binding.vpMessage.setCurrentItem(bannerPosition, false)
    }
    private fun onClickAd(item: Advertise) {
        (requireActivity() as MainActivity).goToActivity(AdvertiseActivity.getIntent(requireContext()))
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

    override fun initEvent() {
        with(binding) {
            swMessageContactX.setOnClickListener {
                messageViewModel.setEvent(MessageContract.MessageEvent.OnClickContactX(swMessageContactX.isSelected))
            }
            swMessageContactO.setOnClickListener {
                messageViewModel.setEvent(MessageContract.MessageEvent.OnClickContactO(swMessageContactO.isSelected))
            }
            cvMessageContactX.setOnClickListener {
                if (swMessageContactX.isSelected) {
                    goToMessageActivity()
                }
            }
            cvMessageContactO.setOnClickListener {
                if (swMessageContactO.isSelected) {
                    goToGroupActivity()
                }
            }
            btMessageContactX.setOnClickListener {
                goToMessageActivity()
            }
            btMessageContactO.setOnClickListener {
                goToGroupActivity()
            }
            binding.vpMessage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bannerPosition = position
                    binding.tvMessageAdIndicator.text = getString(R.string.viewpager2_banner, (bannerPosition % adList.size)+1, adList.size)
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
    }
    fun scrollJobCreate() {
        job = lifecycleScope.launchWhenResumed {
            delay(3000)
            binding.vpMessage.setCurrentItem(++bannerPosition, true)
        }
    }

    override fun onResume() {
        super.onResume()
        scrollJobCreate()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }
    private fun goToMessageActivity() {
    }
    private fun goToContactActivity() {
        //val intent = ContactActivity.getIntent(requireContext())
        //(requireActivity() as MainActivity).goToActivity(intent)
    }
    private fun goToGroupActivity() {
        val intent = GroupActivity.getIntent(requireContext())
        (requireActivity() as MainActivity).goToActivity(intent)
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageViewModel.viewState.collectLatest { state ->
                    binding.swMessageContactX.isSelected = state.contactX
                    binding.swMessageContactO.isSelected = state.contactO
                    binding.btMessageContactX.visibility = if (state.contactX) View.VISIBLE else View.GONE
                    binding.btMessageContactO.visibility = if (state.contactO) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun initData() {
    }
    fun selectTapUI(isSelect: Boolean, textView: AppCompatTextView, view: View) {
        textView.isSelected = isSelect
        view.isSelected = isSelect
    }

    companion object {
        val TAG = "MessageFragment"

        @JvmStatic
        fun newInstance() = MessageFragment()
    }
}