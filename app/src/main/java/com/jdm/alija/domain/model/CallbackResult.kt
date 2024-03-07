package com.jdm.alija.domain.model

data class CallbackResult(
    val mobile: String,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0,
    var name: String = ""
)
