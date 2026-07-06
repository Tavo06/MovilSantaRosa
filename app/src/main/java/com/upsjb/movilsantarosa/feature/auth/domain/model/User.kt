package com.upsjb.movilsantarosa.feature.auth.domain.model

import kotlinx.serialization.Serializable

data class User(
    val firstname: String,
    val lastname: String,
    val email: String,
    val uid: String,
    val rol: UserRole = UserRole.PARTNER
){
    val fullName get() = "$firstname $lastname"
}

@Serializable
enum class UserRole {
    ADMIN,
    PARTNER;

    val displayName: String
        get() = when (this) {
            ADMIN -> "Administrador"
            PARTNER -> "Socio"
        }
}
@Serializable
enum class UserStatus {
    ACTIVE,
    PENDING,
    INACTIVE;

    val displayName: String
        get() = when (this) {
            ACTIVE -> "Activo"
            PENDING -> "Pendiente"
            INACTIVE -> "Inactivo"
        }
}