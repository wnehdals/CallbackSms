package com.jdm.alija.presentation.ui.msg.set

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityMessageBinding
import com.jdm.alija.dialog.ListSelectDialog
import com.jdm.alija.domain.model.SelectData
import com.jdm.alija.presentation.ui.group.detail.GroupDetailContract
import com.jdm.alija.presentation.ui.group.detail.GroupDetailViewModel
import com.jdm.alija.presentation.ui.msg.detail.MessageDetailActivity
import com.jdm.alija.presentation.util.slideRight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessageActivity : BaseActivity<ActivityMessageBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_message
    private val groupDetailViewModel: GroupDetailViewModel by viewModels()
    private val beforeCheckList : List<SelectData> by lazy {
        listOf<SelectData>(
            SelectData(0, getString(R.string.str_is_before_check_option_1), false),
            SelectData(1, getString(R.string.str_is_before_check_option_2), false)
        )
    }
    private val duplicateCheckList : List<SelectData> by lazy {
        listOf<SelectData>(
            SelectData(0, getString(R.string.str_one_to_day), false),
            SelectData(1, getString(R.string.str_one_to_week), false),
            SelectData(2, getString(R.string.str_one_to_month), false)
        )
    }
    private val messageDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            groupDetailViewModel.getDefaultGroup()
        }
    }
    override fun initView() {
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupDetailViewModel.viewState.collectLatest { state ->
                    binding.swNoneMessageIncall.isSelected = state.group.isIncallActive
                    binding.swNoneMessageOutcall.isSelected = state.group.isOutcallActivie
                    binding.swNoneMessageReleasecall.isSelected = state.group.isReleaseCallActive
                    binding.tvNoneMessageDuplicate.text = duplicateCheckList[state.group.dupicateIdx].text
                    binding.tvNoneMessageDuplicateOut.text = duplicateCheckList[state.group.dupicateIdx2].text
                    binding.tvNoneMessageDuplicateRelease.text = duplicateCheckList[state.group.dupicateIdx3].text
                    binding.tvNoneMessageDay0.isSelected = state.group.mon
                    binding.tvNoneMessageDay1.isSelected = state.group.tue
                    binding.tvNoneMessageDay2.isSelected = state.group.wed
                    binding.tvNoneMessageDay3.isSelected = state.group.thu
                    binding.tvNoneMessageDay4.isSelected = state.group.fri
                    binding.tvNoneMessageDay5.isSelected = state.group.sat
                    binding.tvNoneMessageDay6.isSelected = state.group.sun

                    binding.tvMessageBeforeCheck.text = if (state.group.isBeforeCheck)
                        beforeCheckList[0].text
                        else beforeCheckList[1].text
                    binding.tvNoneMessageDayDesc.text = makeDayDescString(state.group.mon, state.group.tue, state.group.wed, state.group.thu, state.group.fri, state.group.sat, state.group.sun)
                }
            }
        }
    }
    private fun makeDayDescString(mon: Boolean, tue: Boolean, wed: Boolean, thu: Boolean, fri: Boolean, sat: Boolean, sun: Boolean): String {
        val list = mutableListOf<String>()
        if (mon) {
            list.add("월")
        }
        if (tue) {
            list.add("화")
        }
        if (wed) {
            list.add("수")
        }
        if (thu) {
            list.add("목")
        }
        if (fri) {
            list.add("금")
        }
        if (sat) {
            list.add("토")
        }
        if (sun) {
            list.add("일")
        }
        if (list.isEmpty()) {
            return "문자 전송을 허용한 요일이 없습니다."
        } else {
            var str = ""
            list.forEachIndexed { index, s ->
                if (index < list.size-1)
                    str += "${s}, "
                else
                    str += "${s}"
            }
            return "${str} 요일만 문자를 전송합니다."
        }

    }

    override fun initEvent() {
        with(binding) {
            ivNoneMessageSet.setOnClickListener {
                finish()
            }
            llNoneMessageIncall.setOnClickListener {
                messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@MessageActivity, id = groupDetailViewModel.id, type = MessageDetailActivity.TYPE_INCALL))
                slideRight()
            }
            llNoneMessageOutcall.setOnClickListener {
                messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@MessageActivity, id = groupDetailViewModel.id, type = MessageDetailActivity.TYPE_OUTCALL))
                slideRight()
            }
            llNoneMessageReleasecall.setOnClickListener {
                messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@MessageActivity, id = groupDetailViewModel.id, type = MessageDetailActivity.TYPE_RELEASECALL))
                slideRight()
            }
            swNoneMessageIncall.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickIncalActiveButton(swNoneMessageIncall.isSelected))
            }
            swNoneMessageOutcall.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickOutcalActiveButton(swNoneMessageOutcall.isSelected))
            }
            swNoneMessageReleasecall.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickReleasecalActiveButton(swNoneMessageReleasecall.isSelected))
            }
            llNoneMessageDuplicate.setOnClickListener {
                ListSelectDialog(
                    title = getString(R.string.str_message_set_title_3),
                    duplicateCheckList,
                    groupDetailViewModel.viewState.value.group.dupicateIdx,
                ) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDuplicate(it))
                }.show(supportFragmentManager, ListSelectDialog.TAG)
            }
            llNoneMessageDuplicateOut.setOnClickListener {
                ListSelectDialog(
                    title = getString(R.string.str_message_set_title_3),
                    duplicateCheckList,
                    groupDetailViewModel.viewState.value.group.dupicateIdx2,
                ) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDuplicate2(it))
                }.show(supportFragmentManager, ListSelectDialog.TAG)
            }
            llNoneMessageDuplicateRelease.setOnClickListener {
                ListSelectDialog(
                    title = getString(R.string.str_message_set_title_3),
                    duplicateCheckList,
                    groupDetailViewModel.viewState.value.group.dupicateIdx3,
                ) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDuplicate3(it))
                }.show(supportFragmentManager, ListSelectDialog.TAG)
            }
            tvNoneMessageDay0.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(0, tvNoneMessageDay0.isSelected))
            }
            tvNoneMessageDay1.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(1, tvNoneMessageDay1.isSelected))
            }
            tvNoneMessageDay2.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(2, tvNoneMessageDay2.isSelected))
            }
            tvNoneMessageDay3.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(3, tvNoneMessageDay3.isSelected))
            }
            tvNoneMessageDay4.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(4, tvNoneMessageDay4.isSelected))
            }
            tvNoneMessageDay5.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(5, tvNoneMessageDay5.isSelected))
            }
            tvNoneMessageDay6.setOnClickListener {
                groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(6, tvNoneMessageDay6.isSelected))
            }
            llNomeMessageBefore.setOnClickListener {
                ListSelectDialog(
                    title = getString(R.string.str_before_check_sms),
                    beforeCheckList,
                    if (groupDetailViewModel.viewState.value.group.isBeforeCheck) 0 else 1
                ) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickBeforeCheck(it))
                }.show(supportFragmentManager, ListSelectDialog.TAG)
            }
        }
    }

    override fun initData() {
        groupDetailViewModel.getDefaultGroup()
    }
    companion object {
        val TAG = "MessageActivity"
        val DIRECTION = "DIRECTION"
        val TYPE = "MessageType"
        val TYPE_INCALL = 1
        val TYPE_OUTCALL = 2
        val TYPE_RELEASECALL = 3
        fun getIntent(context: Context, groupName: String): Intent {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(TYPE, groupName)
            return  intent
        }
    }
}