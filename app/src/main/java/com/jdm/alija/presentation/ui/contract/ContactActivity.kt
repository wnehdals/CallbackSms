package com.jdm.alija.presentation.ui.contract

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityContactBinding
import com.jdm.alija.databinding.FragmentContractBinding
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.presentation.util.controlSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactActivity : BaseActivity<FragmentContractBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_contract
    private val contractViewModel : ContractViewModel by viewModels()
    private val contactAdapter by lazy { ContactAdapter(this, this::onClickContactSwitch) }
    private lateinit var groupName: String
    override fun initView() {
        with(binding) {
            groupName = intent.getStringExtra(GROUP_KEY)?: ""
            rvContact.adapter = contactAdapter
            rvContact.itemAnimator = null
        }
    }

    override fun subscribe() {
        contractViewModel.contactData.observe(this) {
            contactAdapter.submitList(it)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                contractViewModel.effect.collectLatest {
                    when (it) {
                        is ContractContract.ContractSideEffect.GoToBack -> {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun initEvent() {
        binding.ivContractSearch.setOnClickListener {
            contractViewModel.searchKeyword(binding.etContact.text.toString())
            hideKeyboard()
        }
        binding.etContact.addTextChangedListener {
            contractViewModel.searchKeyword(it.toString())
        }
        binding.ivContactAll.setOnClickListener {
            binding.ivContactAll.isSelected = !binding.ivContactAll.isSelected
            contractViewModel.clickAll(binding.ivContactAll.isSelected, binding.etContact.text.toString())
        }
        binding.tvGroupAdd.setOnClickListener {
            contractViewModel.updateGroup()

        }
    }

    override fun initData() {
        val id = intent.getIntExtra(GROUP_KEY, -1)
        if (id == -1) {
            finish()
            return
        }
        contractViewModel.getGroup(id)
    }
    private fun onClickContactSwitch(item: Contact, pos: Int) {
        contractViewModel.clickContactItem(item, binding.etContact.text.toString())
    }
    fun hideKeyboard() {
        this.controlSoftKeyboard(binding.etContact, false)
    }
    companion object {
        val GROUP_KEY = "GROUP_KEY"
        val BUNDLE_KEY = "BUNDLE_KEY"
        fun getIntent(context: Context, groupId: Int): Intent {
            val intent = Intent(context, ContactActivity::class.java)
            intent.putExtra(GROUP_KEY, groupId)
            return intent
        }
    }
}