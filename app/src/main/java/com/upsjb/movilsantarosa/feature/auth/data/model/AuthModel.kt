package com.upsjb.movilsantarosa.feature.auth.data.model

import com.upsjb.movilsantarosa.feature.auth.domain.model.User

data class AuthModel(
    val email: String,
    val uid: String,
)

fun AuthModel.toUser(): User {
    return User(
        firstname = "",
        lastname = "",
        email = email,
        uid = uid
    )
}