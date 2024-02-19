package com.jdm.alija.view

import android.content.Context
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import com.jdm.alija.R
import com.jdm.alija.databinding.ViewTextAreaBinding
import com.jdm.alija.domain.model.TextInputFilter

class TextArea @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {
    private var maxCnt = 1000


    private lateinit var binding: ViewTextAreaBinding




    init {
        initView()

        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.TextArea,
            defStyleAttr,
            0
        ).apply {
            binding.etTextArea.let {
                it.hint = getString(R.styleable.TextArea_android_hint) ?: ""
                it.setText(getString(R.styleable.TextArea_android_text) ?: "")
                it.filters = arrayOf(InputFilter.LengthFilter(maxCnt))
            }

            binding.tvTextAreaMax.text = "/ $maxCnt"
            recycle()
        }

        binding.etTextArea.addTextChangedListener {
            it?.let {
                binding.tvTextAreaCnt.text = "${it.length}"
            }
        }
        binding.etTextArea.setOnFocusChangeListener { _, hasFocus ->
            binding.clTextArea.isSelected = hasFocus
        }
    }

    private fun initView() {
        binding = ViewTextAreaBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setText(s: String) {
        binding.etTextArea.setText(s)
    }

    fun getText(): String = binding.etTextArea.text.toString()

    fun setMaxLength(max: Int) {
        maxCnt = max
        binding.etTextArea.filters = arrayOf(TextInputFilter(), InputFilter.LengthFilter(maxCnt))
        binding.tvTextAreaMax.text = "/ $maxCnt"
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        binding.etTextArea.addTextChangedListener(textWatcher)
    }

    fun removeTextChangedListener(textWatcher: TextWatcher) {
        binding.etTextArea.removeTextChangedListener(textWatcher)
    }
}
