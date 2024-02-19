package com.jdm.alija.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: String,
    val name: String,
    var isSelected: Boolean = false,
    var imgUri: String = "",
    var text: String = "",
    var numbers : MutableList<String> = mutableListOf<String>()
) : Parcelable{
}
