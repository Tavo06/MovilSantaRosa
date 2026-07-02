package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.ui.feature.register.components.ContactInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.PersonalInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterActions
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterHeader
import com.upsjb.movilsantarosa.ui.feature.register.components.SecurityInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.VehicleInfoSection

@Composable
fun RegisterScreen(
    formState: RegisterFormState,
    onFormChange: (RegisterFormState.() -> RegisterFormState) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        RegisterHeader(
            onClick = onLoginClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            PersonalInfoSection(
                firstName = formState.firstName,
                lastName = formState.lastName,
                dniNumber = formState.dniNumber,
                birthdate = formState.birthDate,

                onFirstNameChange = {
                    onFormChange { copy(firstName = it) }
                },

                onLastNameChange = {
                    onFormChange { copy(lastName = it) }
                },

                onDniChange = {
                    onFormChange { copy(dniNumber = it.take(8)) }
                },

                onBirthdateChange = {
                    onFormChange { copy(birthDate = it) }
                }
            )

            ContactInfoSection(
                email = formState.email,
                phone = formState.phone,

                onEmailChange = {
                    onFormChange { copy(email = it) }
                },

                onPhoneChange = {
                    onFormChange { copy(phone = it) }
                }
            )

            VehicleInfoSection(
                plateNumber = formState.plateNumber,
                vehicleColor = formState.vehicleColor,
                licenceNumber = formState.licenceNumber,

                onPlateChange = {
                    onFormChange {
                        copy(
                            plateNumber = it.uppercase()
                        )
                    }
                },

                onColorChange = {
                    onFormChange {
                        copy(vehicleColor = it)
                    }
                },

                onLicenceChange = {
                    onFormChange {
                        copy(licenceNumber = it)
                    }
                }
            )

            SecurityInfoSection(
                password = formState.password,
                onPasswordChange = {
                    onFormChange {
                        copy(password = it)
                    }
                },
            )

            RegisterActions(
                onRegisterClick = onRegisterClick,
                onLoginClick = onLoginClick,
                isLoading = isLoading
            )
        }
    }
}