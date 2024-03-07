package com.jdm.alija.domain.model

data class BlackContact(
    val id: Int = -1,
    val mobile: String = "",
    val name: String = "",
    var isSelected: Boolean = false
)