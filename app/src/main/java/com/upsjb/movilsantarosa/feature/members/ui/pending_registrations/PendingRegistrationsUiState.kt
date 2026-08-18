package com.upsjb.movilsantarosa.feature.members.ui.pending_registrations

import com.upsjb.movilsantarosa.feature.members.domain.model.Member

sealed interface PendingRegistrationsUiState {

    data object Loading : PendingRegistrationsUiState

    data class Success(
        val members: List<Member> = emptyList()
    ) : PendingRegistrationsUiState

    data class Error(
        val message: String
    ) : PendingRegistrationsUiState
}
