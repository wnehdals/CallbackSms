package com.jdm.alija.presentation.ui.blacklist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityBlackListBinding
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.presentation.util.slideRight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlackListActivity : BaseActivity<ActivityBlackListBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_black_list
    private val blackListViewModel: BlackListViewModel by viewModels()
    private val blackListAdapter by lazy { BlacklistAdapter(this, this::onClickDelete) }
    private val searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        initData()
    }
    override fun initView() {
        binding.rvContact.adapter = blackListAdapter
    }

    override fun subscribe() {
        blackListViewModel.blackList.observe(this) {
            if (it != null) {
                blackListAdapter.submitList(it)
            }
        }
    }

    override fun initEvent() {
        binding.fab.setOnClickListener {
            searchLauncher.launch(Intent(this, SearchContactActivity::class.java))
            slideRight()
        }
        binding.ivMessageDetailBack.setOnClickListener {
            finish()
        }
    }


    override fun initData() {
        blackListViewModel.getBlackList()
    }
    private fun onClickDelete(item: BlackContact, pos: Int) {
        blackListViewModel.deleteBlackList(item)
    }
    companion object{
        fun getIntent(context: Context) : Intent {
            return Intent(context, BlackListActivity::class.java)
        }
    }
}