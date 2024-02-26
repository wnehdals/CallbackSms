package com.jdm.alija.presentation.ui.contract

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdm.alija.R
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.FragmentContractBinding
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.presentation.ui.contractdetail.ContactDetailActivity
import com.jdm.alija.presentation.ui.main.MainContract
import com.jdm.alija.presentation.ui.main.MainViewModel
import com.jdm.alija.presentation.util.controlSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContractFragment : BaseFragment<FragmentContractBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_contract
    private val contractViewModel : ContractViewModel by viewModels()
    private lateinit var detailResultLauncher: ActivityResultLauncher<Intent>
    private val contactAdapter by lazy { ContactAdapter(requireContext(), this::onClickContactSwitch) }
    override fun initView() {
        with(binding) {
            rvContact.adapter = contactAdapter
        }
        detailResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.etContact.setText("")
            hideKeyboard()
            binding.etContact.clearFocus()
            initData()
        }
    }

    override fun initEvent() {
        binding.ivContractSearch.setOnClickListener {
            contractViewModel.searchKeyword(binding.etContact.text.toString())
        }
        binding.etContact.addTextChangedListener {
            contractViewModel.searchKeyword(it.toString())
        }
        binding.ivContactBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llContactAllSelect.setOnClickListener {
            if (binding.ivContactAllSelect.isSelected) {
                binding.ivContactAllSelect.isSelected = false
                contractViewModel.allCheck(false)
            } else {
                binding.ivContactAllSelect.isSelected = true
                contractViewModel.allCheck(true)
            }
        }
    }

    override fun subscribe() {

        contractViewModel.contactData.observe(viewLifecycleOwner) {
            contactAdapter.submitList(it)
        }
    }

    override fun initData() {
        contractViewModel.getContractList()
    }
    private fun onClickContact(item: Contact) {
        Log.e(TAG, item.toString())
        if (item.isSelected) {
            val intent = ContactDetailActivity.getContactDetailIntent(requireContext(), item)
            detailResultLauncher.launch(intent)

        }

    }
    private fun onClickContactSwitch(item: Contact, pos: Int) {
        //contractViewModel.insertSms(item, binding.etContact.text.toString())
    }

    fun hideKeyboard() {
        requireContext().controlSoftKeyboard(binding.etContact, false)
    }
    companion object {
        @JvmStatic
        fun newInstance() = ContractFragment()
        val TAG = "ContractFragment"
    }
}