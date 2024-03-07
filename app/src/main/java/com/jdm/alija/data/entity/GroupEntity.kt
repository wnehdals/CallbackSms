package com.jdm.alija.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
@Entity(tableName = "Group")
data class GroupEntity(
    val name: String,
    val contactList: List<ContactEntity> = listOf<ContactEntity>(),
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
    ): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val DEFAULT_GROUP_ID = 1
        fun getDefaultGroup(): GroupEntity {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH) + 1
            val day = Calendar.getInstance().get(Calendar.DATE)
            val group = GroupEntity(name = "CallBack")
            group.id = DEFAULT_GROUP_ID
            return group
        }
    }
}
