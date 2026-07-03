package com.upsjb.movilsantarosa.ui.feature.members

import com.upsjb.movilsantarosa.domain.member.model.Members

sealed class MemberUIState {

    data object Loading : MemberUIState()

    data class Success(
        val members: List<Members>
    ) : MemberUIState()

    data class Error(
        val message: String
    ) : MemberUIState()

}