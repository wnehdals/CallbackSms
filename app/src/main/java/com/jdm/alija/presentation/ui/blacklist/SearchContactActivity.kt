package com.jdm.alija.presentation.ui.blacklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivitySearchContactBinding
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.presentation.util.controlSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchContactActivity : BaseActivity<ActivitySearchContactBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_search_contact
    private val searchContactViewModel: SearchContactViewModel by viewModels()
    private val searchAdapter by lazy { SearchContactAdapter(this, this::onClickContact ) }
    override fun initView() {
        binding.rvContact.adapter = searchAdapter
        binding.rvContact.itemAnimator = null
    }

    override fun subscribe() {
        searchContactViewModel.contactData.observe(this) {
            searchAdapter.submitList(it)
        }
        searchContactViewModel.isFinish.observe(this) {
            finish()
        }
    }

    override fun initEvent() {
        binding.ivContractSearch.setOnClickListener {
            searchContactViewModel.searchKeyword(binding.etContact.text.toString())
            hideKeyboard()
        }
        binding.etContact.addTextChangedListener {
            searchContactViewModel.searchKeyword(it.toString())
        }
        binding.tvContactSearchComplete.setOnClickListener {
            searchContactViewModel.insertBlackList()
        }
        binding.ivMessageDetailBack.setOnClickListener {
            finish()
        }

    }

    override fun initData() {
        searchContactViewModel.getContactList()
    }
    private fun onClickContact(item: BlackContact, pos: Int) {
        searchContactViewModel.clickContactItem(item, binding.etContact.text.toString())
    }
    fun hideKeyboard() {
        this.controlSoftKeyboard(binding.etContact, false)
    }
}