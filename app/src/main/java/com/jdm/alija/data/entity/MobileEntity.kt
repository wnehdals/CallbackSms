package com.jdm.alija.data.entity

data class MobileEntity(
    val id: String,
    val name: String,
    var numbers : MutableList<String> = mutableListOf<String>()
)
