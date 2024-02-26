package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Group")
data class GroupEntity(
    val name: String,
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}