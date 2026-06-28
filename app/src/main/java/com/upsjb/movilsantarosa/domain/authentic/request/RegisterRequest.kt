package com.upsjb.movilsantarosa.domain.authentic.request

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
    val rolUser: RolUser
)

enum class RolUser(val displayName: String) {
    ADMIN("Administrador"),
    PARTNER("Socio")
}