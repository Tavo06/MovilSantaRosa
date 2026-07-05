package com.upsjb.movilsantarosa.feature.fine.domain.model

import com.upsjb.movilsantarosa.feature.fine.data.model.FineModel
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormState

data class Fine(
    val id: String = "",
    val memberEmail: String = "",
    val memberName: String = "",
    val reason: FineReason = FineReason.OTHER,
    val customReason: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val issuedAt: String = "",
    val dueDate: String = "",
    val status: FineStatus = FineStatus.PENDING,
    val createdBy: String = "",
    val createdAt: String = ""
)

fun FineModel.toDomain(): Fine =
    Fine(
        id = id,
        memberEmail = memberEmail,
        memberName = memberName,
        reason = reason,
        customReason = customReason,
        amount = amount,
        description = description,
        issuedAt = issuedAt,
        dueDate = dueDate,
        status = status,
        createdBy = createdBy,
        createdAt = createdAt
    )

fun Fine.toModel(): FineModel =
    FineModel(
        id = id,
        memberEmail = memberEmail,
        memberName = memberName,
        reason = reason,
        customReason = customReason,
        amount = amount,
        description = description,
        issuedAt = issuedAt,
        dueDate = dueDate,
        status = status,
        createdBy = createdBy,
        createdAt = createdAt
    )

fun Fine.toForm(): FineFormState {
    return FineFormState(
        memberEmail = memberEmail,
        memberName = memberName,
        reason = reason,
        customReason = customReason,
        amount = amount.toString(),
        description = description,
        issuedAt = issuedAt,
        dueDate = dueDate,
        status = status
    )
}

fun FineFormState.toDomain(): Fine {
    return Fine(
        memberEmail = memberEmail,
        memberName = memberName,
        reason = reason,
        customReason = customReason,
        amount = amount.toDoubleOrNull() ?: 0.0,
        description = description,
        issuedAt = issuedAt,
        dueDate = dueDate,
        status = status
    )
}