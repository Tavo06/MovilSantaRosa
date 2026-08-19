package com.upsjb.movilsantarosa.feature.members.ui.member_profile

import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment

sealed interface MemberProfileUiState {

    data object Loading : MemberProfileUiState

    data class Success(
        val member: Member,
        val fines: List<Fine> = emptyList(),
        val payments: List<Payment> = emptyList()
    ) : MemberProfileUiState {

        val activeFines: List<Fine>
            get() = fines.filter { it.status == FineStatus.PENDING }

        val lastPayment: Payment?
            get() = payments.maxByOrNull { it.paidAt }
    }

    data class Error(
        val message: String
    ) : MemberProfileUiState
}
