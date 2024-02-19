package com.jdm.alija.domain.model

data class CallType(val status: Status, val displayName: String?) {

    enum class Status {
        CONNECTING,
        DIALING,
        RINGING,
        ACTIVE,
        DISCONNECTED,
        UNKNOWN;
    }
}