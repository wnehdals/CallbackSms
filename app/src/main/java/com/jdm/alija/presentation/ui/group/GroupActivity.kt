package com.jdm.alija.presentation.ui.group

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityGroupBinding
import com.jdm.alija.databinding.FragmentGroupBinding
import com.jdm.alija.dialog.EditTextDialog
import com.jdm.alija.domain.model.Group
import com.jdm.alija.presentation.ui.group.detail.GroupDetailActivity
import com.jdm.alija.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupActivity : BaseActivity<FragmentGroupBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_group
    private val groupViewModel: GroupViewModel by viewModels()
    private val groupAdapter: GroupAdapter by lazy { GroupAdapter(this::onClickGroup, this::onClickDeleteGroup) }
    override fun initView() {
        binding.rvGroup.adapter = groupAdapter
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.viewState.collectLatest { state ->
                    groupAdapter.submitList(state.groupList)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.effect.collectLatest { effect ->
                    when (effect) {
                        is GroupContract.GroupSideEffect.GoToGroupDetail -> {
                            goToActivity(GroupDetailActivity.getIntent(this@GroupActivity, effect.group.id))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            fab.setOnClickListener {
                EditTextDialog(
                    title = getString(R.string.str_create_group),
                    msg = getString(R.string.str_hint_group_name),
                    leftText = getString(R.string.str_cancel),
                    rightText = getString(R.string.str_confirm),
                    leftClick = {},
                    rightClick = {
                        groupViewModel.insertGroup(it)
                    }
                ).show(supportFragmentManager, EditTextDialog.TAG )

            }
            ivGroupBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        groupViewModel.getGroupList()
    }

    override fun initData() {

    }
    private fun onClickGroup(group: Group) {
        goToActivity(GroupDetailActivity.getIntent(this, group.id))
    }
    private fun onClickDeleteGroup(group: Group) {
        groupViewModel.deleteGroup(group)
    }
    companion object {
        val TAG = "GroupActivity"
        fun getIntent(context: Context) : Intent {
            val intent = Intent(context, GroupActivity::class.java)
            return intent
        }
    }
}