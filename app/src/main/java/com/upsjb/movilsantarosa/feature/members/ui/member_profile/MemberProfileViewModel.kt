package com.upsjb.movilsantarosa.feature.members.ui.member_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesByEmailUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetAllMembersUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.GetPaymentsByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemberProfileViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getAllMembersUseCase: GetAllMembersUseCase,
    private val getFinesByEmailUseCase: GetFinesByEmailUseCase,
    private val getPaymentsByEmailUseCase: GetPaymentsByEmailUseCase
) : ViewModel() {

    val uiState = flow {

        val user = currentUserUseCase()
            .getOrElse {
                throw Exception("No existe una sesión activa.")
            }

        emitAll(
            combine(
                getAllMembersUseCase(),
                getFinesByEmailUseCase(user.email),
                getPaymentsByEmailUseCase(user.email)
            ) { members, fines, payments ->

                val member = members.firstOrNull { it.email == user.email }

                if (member == null) {
                    MemberProfileUiState.Error(
                        "No se encontró la información del socio."
                    )
                } else {
                    MemberProfileUiState.Success(
                        member = member,
                        fines = fines,
                        payments = payments
                    )
                }
            }
        )
    }
        .catch { error ->
            emit(
                MemberProfileUiState.Error(
                    error.message ?: "No se pudo cargar el perfil."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MemberProfileUiState.Loading
        )
}
