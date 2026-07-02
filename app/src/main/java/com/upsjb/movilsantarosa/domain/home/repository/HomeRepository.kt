package com.upsjb.movilsantarosa.domain.home.repository

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest

interface HomeRepository {
    suspend fun getUser(
        uid: String
    ): Result<User>

}
