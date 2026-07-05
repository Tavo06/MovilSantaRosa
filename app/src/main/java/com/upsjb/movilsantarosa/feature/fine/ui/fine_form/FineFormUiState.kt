package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import com.upsjb.movilsantarosa.core.utils.currentDateString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.members.domain.model.Member

data class FineFormUiState(
    val form: FineFormState = FineFormState(),
    val selectedMember: Member? = null,
    val actionState: FineFormActionState = FineFormActionState.Idle,
    val mode: FineFormMode = FineFormMode.CREATE
)

data class FineFormState(
    val memberEmail: String = "",
    val memberName: String = "",
    val reason: FineReason = FineReason.OTHER,
    val customReason: String = "",
    val amount: String = "",
    val description: String = "",
    val issuedAt: String = currentDateString(),
    val dueDate: String = "",
    val status: FineStatus = FineStatus.PENDING,
)

sealed interface FineFormActionState {
    data object Idle : FineFormActionState
    data object Loading : FineFormActionState
    data object Success : FineFormActionState
    data class Error(val message: String) : FineFormActionState
}

enum class FineFormMode {
    CREATE,
    EDIT,
    READ_ONLY
}