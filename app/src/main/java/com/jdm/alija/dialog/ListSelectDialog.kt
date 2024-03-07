package com.jdm.alija.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jdm.alija.R
import com.jdm.alija.base.BaseBottomSheetDialogFragment
import com.jdm.alija.databinding.DialogListSelectBinding
import com.jdm.alija.domain.model.SelectData

class ListSelectDialog(
    private val title: String,
    private val data: List<SelectData>,
    private val selectedIdx: Int? = null,
    private val onClickSelectData: (SelectData) -> Unit
) : BaseBottomSheetDialogFragment<DialogListSelectBinding>() {

    override val layoutResId: Int
        get() = R.layout.dialog_list_select
    override var heightPercent: Float = 0.8f
    private val selectAdapter by lazy { SelectAdapter(requireContext(), this::onClickItem) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.TopRoundBottomDialog)
            .apply {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
    }
    override fun initView(view: View) {
        with(binding) {
            rvSelectList.adapter = selectAdapter
            rvSelectList.itemAnimator = null
            tvPlayerSetTitle.text = title
        }
    }

    override fun initEvent() {
        with(binding) {
            ivDialogPlayerSet.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun subscribe() {
    }

    override fun initData() {
        Log.e(TAG, "${data.toString()} / $selectedIdx")
        data.forEach { it.isSelected = false }
        if (selectedIdx != null) {
            data.forEach {
                if (it.id == selectedIdx) it.isSelected = true }
        }
        selectAdapter.submitList(data)
    }
    private fun onClickItem(item: SelectData) {
        onClickSelectData(item)
        dismiss()
    }
    companion object {
        val TAG = "ListSelectDialog"
    }
}
