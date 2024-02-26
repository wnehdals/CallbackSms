package com.jdm.alija.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: String,
    val name: String,
    var isSelected: Boolean = false,
    var isEnable: Boolean = true,
    var imgUri: String = "",
    var text: String = "",
    var isKakao: Boolean = false,
    var isCheck: Boolean = false,
    var numbers : MutableList<String> = mutableListOf<String>(),
    var mobile: String = "",
    var groupName: String = "",
    var isIncall: Boolean = false,
    var isOutCall: Boolean = false,
    var isReleaseCall: Boolean = false
) : Parcelable{
}
