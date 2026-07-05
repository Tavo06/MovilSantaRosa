package com.upsjb.movilsantarosa.feature.payments.domain.model

import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentMethod
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentModel


data class Payment(
    val id: String = "",
    val fineId: String = "",

    val memberEmail: String = "",
    val memberName: String = "",

    val amount: Double = 0.0,

    val paymentMethod: PaymentMethod = PaymentMethod.CASH,

    val paidAt: String = "",

    val observation: String = "",

    val createdBy: String = "",
    val createdAt: String = "",
    val memberDniNumber: String = ""
)

fun PaymentModel.toDomain(): Payment =
    Payment(
        id = id,
        fineId = fineId,
        memberEmail = memberEmail,
        memberName = memberName,
        amount = amount,
        paymentMethod = paymentMethod,
        paidAt = paidAt,
        observation = observation,
        createdBy = createdBy,
        createdAt = createdAt,
        memberDniNumber = memberDniNumber
    )

fun Payment.toModel(): PaymentModel =
    PaymentModel(
        id = id,
        fineId = fineId,
        memberEmail = memberEmail,
        memberName = memberName,
        amount = amount,
        paymentMethod = paymentMethod,
        paidAt = paidAt,
        observation = observation,
        createdBy = createdBy,
        createdAt = createdAt,
        memberDniNumber = memberDniNumber
    )
