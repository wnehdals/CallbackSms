package com.jdm.alija.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Version(
    val minimum : String = "1.0.0",
    val latest: String = "1.0.0"
)
