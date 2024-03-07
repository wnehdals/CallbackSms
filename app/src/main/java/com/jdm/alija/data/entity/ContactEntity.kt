package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.Year

@Parcelize
@Entity(tableName = "Contact")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val mobile: String,
    val groupName: String,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0
): Parcelable
