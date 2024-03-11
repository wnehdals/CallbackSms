package com.jdm.alija.dialog

import android.view.View
import com.jdm.alija.R
import com.jdm.alija.base.BaseDialogFragment
import com.jdm.alija.databinding.DialogLoadingBinding

class LoadingDialog(

) : BaseDialogFragment<DialogLoadingBinding>(){
    override val layoutResId: Int
        get() = R.layout.dialog_loading

    override fun initView(view: View) {
        isCancelable = false
    }

    override fun initEvent() {
    }

    override fun subscribe() {
    }

    override fun initData() {
    }
    companion object {
        val TAG = "LoadingDialog"
    }
}