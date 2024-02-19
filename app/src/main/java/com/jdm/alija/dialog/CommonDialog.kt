package com.jdm.alija.dialog

import android.view.View
import com.jdm.alija.R
import com.jdm.alija.base.BaseDialogFragment
import com.jdm.alija.databinding.DialogCommonEvenBinding

class CommonDialog(
    private val title: String,
    private val msg: String,
    private val leftText: String? = null,
    private val rightText: String,
    private val leftClick: (() -> Unit)? = null,
    private val rightClick: () -> Unit,
    private val isCancel: Boolean = true
) : BaseDialogFragment<DialogCommonEvenBinding>() {
    override val layoutResId: Int
        get() = R.layout.dialog_common_even

    override fun initView(view: View) {
        isCancelable = isCancel
        with(binding) {
            commonDialogEvenTitle.text = title
            commonDialogEvenMsg.text = msg
            tvDialogCommonEvenRight.text = rightText

            if (leftText != null) {
                tvDialogCommonEvenLeft.visibility = View.VISIBLE
                tvDialogCommonEvenLeft.text = leftText
            }
        }
    }

    override fun initEvent() {
        with(binding) {
            tvDialogCommonEvenRight.setOnClickListener {
                dialog?.dismiss()
                rightClick()
            }
            tvDialogCommonEvenLeft.setOnClickListener {
                dialog?.dismiss()
                leftClick?.let { it() }
            }
        }
    }

    override fun subscribe() {
    }

    override fun initData() {
    }

    companion object {
        val TAG = this.javaClass.simpleName
    }
}
