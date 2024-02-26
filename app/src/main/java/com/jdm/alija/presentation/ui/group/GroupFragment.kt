package com.jdm.alija.presentation.ui.group

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentGroupBinding
import com.jdm.alija.dialog.EditTextDialog
import com.jdm.alija.domain.model.Group
import com.jdm.alija.presentation.ui.main.MainActivity
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding>() {
    private val groupViewModel: GroupViewModel by viewModels()
    private val mainViewModel : MainViewModel by activityViewModels()

    override var onBackPressedCallback: OnBackPressedCallback? = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickHomeButton)
        }
    }
    private val groupAdapter: GroupAdapter by lazy { GroupAdapter(this::onClickGroup, this::onClickDeleteGroup) }
    override val layoutResId: Int
        get() = R.layout.fragment_group

    override fun initView() {
        binding.rvGroup.adapter = groupAdapter
    }

    override fun initEvent() {
        with(binding) {
            fab.setOnClickListener {
                EditTextDialog(
                    title = "",
                    msg = getString(R.string.str_hint_group_name),
                    leftText = getString(R.string.str_cancel),
                    rightText = getString(R.string.str_confirm),
                    leftClick = {},
                    rightClick = {
                        groupViewModel.insertGroup(it)
                    }
                ).show(parentFragmentManager, EditTextDialog.TAG )

            }
        }
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                groupViewModel.viewState.collectLatest { state ->
                    groupAdapter.submitList(state.groupList)
                }
            }
        }
    }

    override fun initData() {
        groupViewModel.getGroupList()
    }
    private fun onClickGroup(group: Group) {
        val intent = Intent(requireContext(), GroupActivity::class.java)
        (requireActivity() as MainActivity).goToActivity(intent)
    }
    private fun onClickDeleteGroup(group: Group) {
        groupViewModel.deleteGroup(group)
    }

    companion object {
        val TAG = "GroupFragment"
        @JvmStatic
        fun newInstance() = GroupFragment()
    }
}