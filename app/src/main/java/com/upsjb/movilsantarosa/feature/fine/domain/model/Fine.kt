package com.upsjb.movilsantarosa.feature.fine.domain.model

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.core.utils.toDoubleSafe
import com.upsjb.movilsantarosa.feature.fine.data.model.FineModel
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormState

data class Fine(
    val id: String = "",
    val memberEmail: String = "",
    val memberName: String = "",
    val memberDniNumber: String = "",
    val reason: FineReason = FineReason.OTHER,
    val customReason: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val issuedAt: Long = currentTimeMillis(),
    val dueDate: Long = currentTimeMillis(),
    val status: FineStatus = FineStatus.PENDING,
    val createdBy: String = "",
    val createdAt: Long = currentTimeMillis()
)

fun FineModel.toDomain(): Fine =
    Fine(
        id = id,
        memberEmail = memberEmail,
        memberName = memberName,
        memberDniNumber = memberDniNumber,
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
        memberDniNumber = memberDniNumber,
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
        id = id,
        memberEmail = memberEmail,
        memberName = memberName,
        memberDniNumber = memberDniNumber,
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
        id = id,
        memberEmail = memberEmail,
        memberName = memberName,
        memberDniNumber = memberDniNumber,
        reason = reason,
        customReason = customReason,
        amount = amount.toDoubleSafe(),
        description = description,
        issuedAt = issuedAt,
        dueDate = dueDate,
        status = status
    )
}