
package com.upsjb.movilsantarosa.domain.models

data class PaymentMember(
    val id: String,
    val fullName: String,
    val dni: String,
    val isActive: Boolean = true
)

data class PaymentHistory(
    val month: String,
    val year: Int,
    val amount: Double,
    val isPaid: Boolean
)

data class PaymentDetail(
    val member: PaymentMember,
    val totalDebt: Double,
    val paymentHistory: List<PaymentHistory>
)

enum class PaymentTab {
    HOME,
    MEMBERS,
    PAYMENTS,
    FINES,
    ANNOUNCEMENTS
}