package com.jdm.alija.dialog

import android.text.InputFilter
import android.view.View
import com.jdm.alija.R
import com.jdm.alija.base.BaseDialogFragment
import com.jdm.alija.databinding.DialogEditTextBinding
import com.jdm.alija.presentation.util.TextInputFilter

class EditTextDialog(
    private val title: String,
    private val msg: String,
    private val leftText: String? = null,
    private val rightText: String,
    private val leftClick: (() -> Unit)? = null,
    private val rightClick: (String) -> Unit,
    private val isCancel: Boolean = true
) : BaseDialogFragment<DialogEditTextBinding>() {
    override val layoutResId: Int
        get() = R.layout.dialog_edit_text

    override fun initView(view: View) {
        isCancelable = isCancel
        with(binding) {
            commonDialogEvenTitle.text = title
            commonDialogEvenMsg.text = msg
            tvDialogCommonEvenRight.text = rightText

            if (leftText != null) {
                tvDialogCommonEvenLeft.visibility = android.view.View.VISIBLE
                tvDialogCommonEvenLeft.text = leftText
            }
            etDialogEdit.filters = arrayOf(TextInputFilter(), InputFilter.LengthFilter(30))
        }
    }

    override fun initEvent() {
        with(binding) {
            tvDialogCommonEvenRight.setOnClickListener {
                dialog?.dismiss()
                rightClick(etDialogEdit.text.toString())
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
