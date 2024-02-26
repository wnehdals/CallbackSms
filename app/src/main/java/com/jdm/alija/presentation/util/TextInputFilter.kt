package com.jdm.alija.presentation.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * 한글, 영어, 특수문자, 숫자만 허용
 */
class TextInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val pt = Pattern.compile("^[ㄱ-ㅣ가-힣|a-zA-Z|0-9|`₩~!@#\$%<>^&*()\\-=+_?:;\"',.{}|\\[\\]/\\\\]+$")
        return if (!pt.matcher(source).matches()) {
            ""
        } else {
            source ?: ""
        }
    }
}
