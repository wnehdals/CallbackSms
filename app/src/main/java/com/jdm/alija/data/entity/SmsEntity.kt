package com.jdm.alarmlocation.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CallbackSms")
data class SmsEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val imgUri: String,
    val text: String,
    val mobile: String,
    val isKakao: Boolean
) {
}