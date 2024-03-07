package com.jdm.alija.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    val id: Int = -1,
    val name: String,
    val contactList: List<Contact> = listOf(),
    val isIncallActive: Boolean = false,
    val isOutcallActivie: Boolean = false,
    val isReleaseCallActive: Boolean = false,
    val dupicateIdx: Int = 0,
    val incallText: String = "",
    val incallImg: String = "",
    val outcallText: String = "",
    val outcallImg: String = "",
    val releaseCallText: String = "",
    val releaseCallImg: String = "",
    val mon: Boolean = false,
    val tue: Boolean = false,
    val wed: Boolean = false,
    val thu: Boolean = false,
    val fri: Boolean = false,
    val sat: Boolean = false,
    val sun: Boolean = false,
    val isBeforeCheck: Boolean = true
    ): Parcelable