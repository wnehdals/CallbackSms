package com.jdm.alija.data.entity

import androidx.room.TypeConverter
import com.google.gson.Gson

class TypeResponseConverter {
    @TypeConverter
    fun fromString(value: String): List<ContactEntity> {
        return Gson().fromJson(value, Array<ContactEntity>::class.java).toList()
    }

    @TypeConverter
    fun fromInfoType(type: List<ContactEntity>): String {
        return Gson().toJson(type)
    }
}
