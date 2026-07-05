package com.upsjb.movilsantarosa.feature.members.domain.model

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.members.data.model.MemberModel
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
    val rol: UserRole = UserRole.PARTNER
) {
    val letterName: String
        get() = "${firstname.firstOrNull() ?: ""}${lastname.firstOrNull() ?: ""}".uppercase()

    val fullName: String
        get() = "$firstname $lastname"
}

fun MemberModel.toDomain(): Member =
    Member(
        birthdate = birthdate,
        dniNumber = dniNumber,
        email = email,
        firstname = firstname,
        lastname = lastname,
        licenceNumber = licenceNumber,
        phone = phone,
        plateNumber = plateNumber,
        vehicleColor = vehicleColor,
        rol = rol
    )