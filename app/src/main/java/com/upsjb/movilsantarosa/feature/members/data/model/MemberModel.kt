package com.upsjb.movilsantarosa.feature.members.data.model

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class MemberModel(
    val birthdate: String = "",
    val dniNumber: String = "",
    val email: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val licenceNumber: String = "",
    val phone: String = "",
    val plateNumber: String = "",
    val vehicleColor: String = "",
    val rol: UserRole = UserRole.SOCIO
)