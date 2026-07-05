package com.upsjb.movilsantarosa.feature.auth.domain.model

import kotlinx.serialization.Serializable

data class User(
    val firstname: String,
    val email: String,
    val uid: String,
    val rol: UserRole = UserRole.SOCIO
)

@Serializable
enum class UserRole {
    @Serializable
    ADMINISTRADOR,

    @Serializable
    SOCIO,
}

@Serializable
enum class UserStatus {
    @Serializable
    ACTIVO,

    @Serializable
    PENDIENTE,

    @Serializable
    INACTIVO,
}