// ui/feature/register/RegisterUIState.kt
package com.upsjb.movilsantarosa.ui.feature.register

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser

sealed class RegisterUIState {
    object Idle : RegisterUIState()
    object Loading : RegisterUIState()
    data class Success(val message: User) : RegisterUIState()
    data class Error(val message: String) : RegisterUIState()
}

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dniNumber: String = "",
    val birthdate: String = "",
    val phone: String = "",
    val plateNumber: String = "",
    val licenceNumber: String = "",
    val vehicleColor: String = "",
    val rolUser: RolUser = RolUser.PARTNER,
    val rolUserDisplayName: String = RolUser.PARTNER.displayName,
    val errors: Map<String, String> = emptyMap()
)