package com.upsjb.movilsantarosa.feature.members.ui

import com.upsjb.movilsantarosa.feature.members.domain.model.Member

sealed class MemberUIState {

    data object Loading : MemberUIState()

    data class Success(
        val query: String = "",
        val members: List<Member> = emptyList()
    ) : MemberUIState()

    data class Error(
        val message: String
    ) : MemberUIState()

}