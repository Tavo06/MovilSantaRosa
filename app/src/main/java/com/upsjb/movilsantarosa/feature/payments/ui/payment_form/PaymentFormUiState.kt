package com.upsjb.movilsantarosa.feature.payments.ui.payment_form

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentMethod

data class PaymentFormState(
    val id: String = "",
    val fineId: String = "",

    val memberEmail: String = "",
    val memberName: String = "",
    val memberDniNumber: String = "",

    val fineAmount: Double = 0.0,
    val fineReason: FineReason = FineReason.OTHER,
    val fineCustomReason: String = "",
    val fineIssuedAt: Long = 0L,
    val fineDueDate: Long = 0L,
    val fineStatus: FineStatus = FineStatus.PENDING,

    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paidAt: Long = currentTimeMillis(),
    val observation: String = ""
) {
    val isMemberFilled: Boolean
        get() = memberEmail.isNotBlank() &&
                memberName.isNotBlank() &&
                memberDniNumber.isNotBlank()

    val isFineFilled: Boolean
        get() = fineId.isNotBlank()
}

data class PaymentFormUiState(
    val paymentId: String = "",
    val fineId: String = "",

    val form: PaymentFormState = PaymentFormState(),

    val actionState: PaymentFormActionState = PaymentFormActionState.Idle,

    val mode: PaymentFormMode = PaymentFormMode.CREATE,
    val role: UserRole = UserRole.PARTNER,
)

sealed interface PaymentFormActionState {
    data object Idle : PaymentFormActionState
    data object Loading : PaymentFormActionState
    data object Success : PaymentFormActionState
    data class Error(val message: String) : PaymentFormActionState
}

enum class PaymentFormMode {
    CREATE,
    EDIT,
    READ_ONLY;

    val displayName: String
        get() = when (this) {
            CREATE -> "Registrar pago"
            EDIT -> "Editar pago"
            READ_ONLY -> "Detalle de pago"
        }

    val displayButton: String
        get() = when (this) {
            CREATE -> "Registrar"
            EDIT -> "Actualizar"
            READ_ONLY -> ""
        }
}