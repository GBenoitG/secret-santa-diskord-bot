package com.bendev.ssdb.utils.properties

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
        val token: String,
        @SerialName("playing_at")
        val playingAt: String,
        @SerialName("allowed_roles_list")
        var allowedRolesName: MutableList<String>,
        @SerialName("allowed_users_id_list")
        var allowedUsersId: MutableList<String>,
        @SerialName("language")
        val localeLanguage: String,
        @SerialName("role_name")
        val roleName: String
)