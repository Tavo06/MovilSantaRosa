package com.upsjb.movilsantarosa.feature.home.domain.model

data class MemberFinancialStatus(
    val activeFineCount: Int = 0
) {
    val isUpToDate: Boolean
        get() = activeFineCount == 0
}
