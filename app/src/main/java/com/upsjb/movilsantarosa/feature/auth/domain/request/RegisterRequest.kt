package com.upsjb.movilsantarosa.feature.auth.domain.request

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val dniNumber: String,
    val birthdate: String,
    val phone: String,
    val plateNumber: String,
    val licenceNumber: String,
    val vehicleColor: String,
    val role: UserRole,
    val status: UserStatus,
)