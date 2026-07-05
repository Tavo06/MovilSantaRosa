package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import com.upsjb.movilsantarosa.core.utils.currentDateString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus

data class FineFormUiState(
    val fineId: String = "",
    val form: FineFormState = FineFormState(),
    val actionState: FineFormActionState = FineFormActionState.Idle,
    val mode: FineFormMode = FineFormMode.CREATE
)

data class FineFormState(
    val id: String = "",
    val memberEmail: String = "",
    val memberName: String = "",
    val memberDniNumber: String = "",
    val reason: FineReason = FineReason.OTHER,
    val customReason: String = "",
    val amount: String = "",
    val description: String = "",
    val issuedAt: String = currentDateString(),
    val dueDate: String = "",
    val status: FineStatus = FineStatus.PENDING,
){
    val isMemberFilled: Boolean
        get() = memberEmail.isNotBlank() &&
                memberName.isNotBlank() &&
                memberDniNumber.isNotBlank()
}

sealed interface FineFormActionState {
    data object Idle : FineFormActionState
    data object Loading : FineFormActionState
    data object Success : FineFormActionState
    data class Error(val message: String) : FineFormActionState
}

enum class FineFormMode {
    CREATE,
    EDIT,
    READ_ONLY;

    val displayName: String
        get() = when (this) {
            CREATE -> "Registrar multa"
            EDIT -> "Editar multa"
            READ_ONLY -> "Detalle de multa"
        }
    val displayButton: String
        get() = when (this) {
            CREATE -> "Registrar"
            EDIT -> "Actualizar"
            READ_ONLY -> ""
        }
}