package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Contact")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val imgUri: String,
    val text: String,
    val mobile: String,
    val isKakao: Boolean,
    val isCheck: Boolean,
    val groupName: String,
    val isIncall: Boolean,
    val isOutCall: Boolean,
    val isReleaseCall: Boolean
): Parcelable
