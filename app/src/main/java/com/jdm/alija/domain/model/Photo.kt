package com.jdm.alija.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    var id: Long = 0L,
    val uri: Uri? = null,
    var dateAddedSecond: Long = 0L,
    var size: Long,
    var isSelected : Boolean = false
) : Parcelable, Comparable<Photo> {

    override fun compareTo(other: Photo): Int {
        return (other.dateAddedSecond - this.dateAddedSecond).toInt()
    }
}
