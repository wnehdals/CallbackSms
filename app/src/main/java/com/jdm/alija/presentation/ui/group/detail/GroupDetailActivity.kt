package com.jdm.alija.presentation.ui.group.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.FragmentGroupDetailBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.dialog.ListSelectDialog
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.SelectData
import com.jdm.alija.presentation.ui.contract.ContactActivity
import com.jdm.alija.presentation.ui.msg.detail.MessageDetailActivity
import com.jdm.alija.presentation.util.slideRight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GroupDetailActivity : BaseActivity<FragmentGroupDetailBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_group_detail
    private val groupDetailViewModel: GroupDetailViewModel by viewModels()
    private val contactLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            groupDetailViewModel.getGroup(groupDetailViewModel.id)
        }
    }


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
            groupDetailViewModel.getGroup(groupDetailViewModel.id)
        }
    }
    override fun initView() {
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupDetailViewModel.viewState.collectLatest { state ->
                    binding.tvGroupDetailContactCnt.text = "${state.group.contactList.size}개"
                    binding.swGroupDetailIncall.isSelected = state.group.isIncallActive
                    binding.swGroupDetailOutcall.isSelected = state.group.isOutcallActivie
                    binding.swGroupDetailReleasecall.isSelected = state.group.isReleaseCallActive
                    binding.tvGroupDetailDuplicate.text = duplicateCheckList[state.group.dupicateIdx].text
                    binding.tvGroupDetailDay0.isSelected = state.group.mon
                    binding.tvGroupDetailDay1.isSelected = state.group.tue
                    binding.tvGroupDetailDay2.isSelected = state.group.wed
                    binding.tvGroupDetailDay3.isSelected = state.group.thu
                    binding.tvGroupDetailDay4.isSelected = state.group.fri
                    binding.tvGroupDetailDay5.isSelected = state.group.sat
                    binding.tvGroupDetailDay6.isSelected = state.group.sun

                    binding.tvGroupDetailBeforeCheck.text = if (state.group.isBeforeCheck)
                        beforeCheckList[0].text
                    else beforeCheckList[1].text
                }
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            ivGroupDetailSet.setOnClickListener {
                finish()
            }
            llGroupDetailContact.setOnClickListener {
                contactLauncher.launch(ContactActivity.getIntent(this@GroupDetailActivity, groupDetailViewModel.id))
                slideRight()
            }
            llGroupDetailIncall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@GroupDetailActivity, MessageDetailActivity.TYPE_INCALL, groupDetailViewModel.id))
                    slideRight()
                } else {
                    showSelectedContactDialog()
                }

            }
            llGroupDetailOutcall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@GroupDetailActivity, MessageDetailActivity.TYPE_OUTCALL, groupDetailViewModel.id))
                    slideRight()
                } else {
                    showSelectedContactDialog()
                }

            }
            llGroupDetailReleasecall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    messageDetailLauncher.launch(MessageDetailActivity.getIntent(this@GroupDetailActivity, MessageDetailActivity.TYPE_RELEASECALL, groupDetailViewModel.id))
                    slideRight()
                } else {
                    showSelectedContactDialog()
                }
            }
            swGroupDetailIncall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickIncalActiveButton(swGroupDetailIncall.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            swGroupDetailOutcall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickOutcalActiveButton(swGroupDetailOutcall.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            swGroupDetailReleasecall.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickReleasecalActiveButton(swGroupDetailReleasecall.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            llGroupDetailDuplicate.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    ListSelectDialog(
                        title = getString(R.string.str_message_set_title_3),
                        duplicateCheckList,
                        groupDetailViewModel.viewState.value.group.dupicateIdx,
                    ) {
                        groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDuplicate(it))
                    }.show(supportFragmentManager, ListSelectDialog.TAG)
                } else {
                    showSelectedContactDialog()
                }

            }
            tvGroupDetailDay0.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(0, tvGroupDetailDay0.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            tvGroupDetailDay1.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(1, tvGroupDetailDay1.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            tvGroupDetailDay2.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(2, tvGroupDetailDay2.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            tvGroupDetailDay3.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(3, tvGroupDetailDay3.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            tvGroupDetailDay4.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(4, tvGroupDetailDay4.isSelected))
                } else {
                    showSelectedContactDialog()
                }

            }
            tvGroupDetailDay5.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(5, tvGroupDetailDay5.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            tvGroupDetailDay6.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickDay(6, tvGroupDetailDay6.isSelected))
                } else {
                    showSelectedContactDialog()
                }
            }
            llGroupDetailBefore.setOnClickListener {
                if (groupDetailViewModel.isContractSelected()) {
                    ListSelectDialog(
                        title = getString(R.string.str_before_check_sms),
                        beforeCheckList,
                        if (groupDetailViewModel.viewState.value.group.isBeforeCheck) 0 else 1
                    ) {
                        groupDetailViewModel.setEvent(GroupDetailContract.GroupDetailEvent.OnClickBeforeCheck(it))
                    }.show(supportFragmentManager, ListSelectDialog.TAG)
                } else {
                    showSelectedContactDialog()
                }
            }
        }

    }
    private fun showSelectedContactDialog() {
        CommonDialog(
            title = getString(R.string.str_guide),
            msg = getString(R.string.str_select_contact_first),
            rightText = getString(R.string.str_confirm),
            rightClick = {}
        ).show(supportFragmentManager, CommonDialog.TAG)
    }

    override fun initData() {
        val id = intent.getIntExtra(GROUP, -1)
        Log.e(TAG, id.toString())
        if (id == -1) {
            finish()
            return
        }
        groupDetailViewModel.getGroup(id)
    }
    companion object {
        val TAG = "GroupDetailActivity"
        val GROUP = "Group"
        fun getIntent(context: Context, groupId: Int) : Intent {
            val intent = Intent(context, GroupDetailActivity::class.java)
            intent.putExtra(GROUP, groupId)
            return intent
        }
    }
}