package com.upsjb.movilsantarosa.data.authentic.model

import com.upsjb.movilsantarosa.domain.authentic.model.User

data class AuthModel(
    val email: String,
    val uid: String,
)

fun AuthModel.toUser(): User {
    return User(
        firstname = "",
        email = email,
        uid = uid
    )
}