package com.jdm.alija.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.jdm.alija.R

abstract class NCompoundButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @DrawableRes imageDrawableId: Int
) : LinearLayout(context, attributeSet, defStyleAttr) {
    private var imageView: AppCompatImageView = AppCompatImageView(context, attributeSet, defStyleAttr)


    var isChecked = false
        set(value) {
            imageView.isSelected = value
            field = value
        }
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        imageView.isEnabled = enabled
    }
    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.NCompoundButton,
            defStyleAttr,
            0
        ).apply {

            imageView.setImageDrawable(ContextCompat.getDrawable(context, imageDrawableId))

            isChecked = getBoolean(R.styleable.NCompoundButton_android_checked, false)
            isEnabled = getBoolean(R.styleable.NCompoundButton_android_enabled, true)


            addView(imageView)
            recycle()
        }
    }
}