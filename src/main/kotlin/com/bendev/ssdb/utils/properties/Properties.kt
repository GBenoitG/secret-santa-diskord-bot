package com.bendev.ssdb.utils.properties

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
        var token: String,
        @SerialName("playing_at")
        var playingAt: String
)