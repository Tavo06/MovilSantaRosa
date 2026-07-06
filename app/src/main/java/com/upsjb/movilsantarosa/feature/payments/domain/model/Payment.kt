package com.upsjb.movilsantarosa.feature.payments.domain.model

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentMethod
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentModel
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormState

data class Payment(
    val id: String = "",
    val fineId: String = "",

    val memberEmail: String = "",
    val memberName: String = "",

    val amount: Double = 0.0,

    val paymentMethod: PaymentMethod = PaymentMethod.CASH,

    val paidAt: Long = currentTimeMillis(),

    val observation: String = "",

    val createdBy: String = "",
    val createdAt: Long = currentTimeMillis(),
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

fun Payment.toForm(): PaymentFormState =
    PaymentFormState(
        id = id,
        fineId = fineId,
        memberEmail = memberEmail,
        memberName = memberName,
        memberDniNumber = memberDniNumber,
        fineAmount = amount,
        paymentMethod = paymentMethod,
        paidAt = paidAt,
        observation = observation
    )

fun PaymentFormState.toDomain(): Payment =
    Payment(
        id = id,
        fineId = fineId,
        memberEmail = memberEmail,
        memberName = memberName,
        memberDniNumber = memberDniNumber,
        amount = fineAmount,
        paymentMethod = paymentMethod,
        paidAt = paidAt,
        observation = observation
    )