package com.jdm.alija.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Contact(
    val id: String,
    val name: String = "Unknown",
    var isSelected: Boolean = false,
    var isEnable: Boolean = true,
    var mobile: String = "",
    var groupName: String = "",
    ) : Parcelable{
}
