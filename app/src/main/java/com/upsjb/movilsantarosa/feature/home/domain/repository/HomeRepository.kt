package com.upsjb.movilsantarosa.feature.home.domain.repository

import com.upsjb.movilsantarosa.feature.auth.domain.model.User

interface HomeRepository {
    suspend fun getUser(
        uid: String
    ): Result<User>

}
