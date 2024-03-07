package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Black")
data class BlackEntity(
    val mobile: String,
    val name: String,
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}