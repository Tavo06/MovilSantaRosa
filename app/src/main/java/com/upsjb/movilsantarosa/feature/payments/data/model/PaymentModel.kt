package com.upsjb.movilsantarosa.feature.payments.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentModel(
    val id: String = "",
    val fineId: String = "",

    val memberEmail: String = "",
    val memberName: String = "",

    val amount: Double = 0.0,

    val paymentMethod: PaymentMethod = PaymentMethod.CASH,

    val paidAt: Long = 0L,

    val observation: String = "",

    val createdBy: String = "",
    val createdAt: Long = 0L,
    val memberDniNumber: String= ""
)

@Serializable
enum class PaymentMethod {
    CASH,
    YAPE,
    PLIN,
    BANK_TRANSFER;

    val displayName: String
        get() = when (this) {
            CASH -> "Efectivo"
            YAPE -> "Yape"
            PLIN -> "Plin"
            BANK_TRANSFER -> "Tranferencia Bancaria"
        }

}