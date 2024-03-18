package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.Year

@Parcelize
@Entity(tableName = "Contact")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val mobile: String,
    val groupName: String,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0,

    val year2: Int = 0,
    val month2: Int = 0,
    val day2: Int = 0,
    val hour2: Int = 0,
    val minute2: Int = 0,

    val year3: Int = 0,
    val month3: Int = 0,
    val day3: Int = 0,
    val hour3: Int = 0,
    val minute3: Int = 0,
): Parcelable
