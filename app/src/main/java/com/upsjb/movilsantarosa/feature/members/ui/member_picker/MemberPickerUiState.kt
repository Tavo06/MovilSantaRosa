package com.upsjb.movilsantarosa.feature.members.ui.member_picker

import com.upsjb.movilsantarosa.feature.members.domain.model.Member

sealed interface MemberPickerUiState {

    data object Loading : MemberPickerUiState

    data class Success(
        val query: String = "",
        val members: List<Member> = emptyList()
    ) : MemberPickerUiState {

        val filteredMembers: List<Member>
            get() = if (query.isBlank()) {
                members
            } else {
                members.filter {
                    it.fullName.contains(query, true) ||
                            it.email.contains(query, true) ||
                            it.dniNumber.contains(query)
                }
            }
    }

    data class Error(
        val message: String
    ) : MemberPickerUiState
}