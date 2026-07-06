package com.upsjb.movilsantarosa.feature.members.ui.members

import com.upsjb.movilsantarosa.feature.members.domain.model.Member

sealed class MemberUiState {

    data object Loading : MemberUiState()

    data class Success(
        val query: String = "",
        val members: List<Member> = emptyList()
    ) : MemberUiState() {
        val filteredMembers: List<Member>
            get() = if (query.isBlank()) {
                members
            } else {
                members.filter {
                    it.firstname.contains(query, true) ||
                            it.fullName.contains(query, true) ||
                            it.dniNumber.contains(query, true)

                }
            }
    }

    data class Error(
        val message: String
    ) : MemberUiState()

}