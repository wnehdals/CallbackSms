package com.jdm.alija.domain.model

import android.media.session.MediaSession.Token
import android.net.Uri

data class MessageInfo(
    var token: Long = 0,
    var location: Uri? = null,
    var bytes: ByteArray = byteArrayOf()
)
