package com.upsjb.movilsantarosa.ui.feature.members

import com.upsjb.movilsantarosa.domain.member.model.Member

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