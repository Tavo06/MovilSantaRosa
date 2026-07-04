package com.upsjb.movilsantarosa.domain.member.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val birthdate: String = "",
    val dniNumber: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val licenceNumber: String = "",
    val phone: String = "",
    val plateNumber: String = "",
    val vehicleColor: String = "",
    val rolUser: String = ""
) {
    val lettername: String
        get() = "${firstname.firstOrNull() ?: ""}${lastname.firstOrNull() ?: ""}".uppercase()

    val fullname: String
        get() = "$firstname $lastname"
}