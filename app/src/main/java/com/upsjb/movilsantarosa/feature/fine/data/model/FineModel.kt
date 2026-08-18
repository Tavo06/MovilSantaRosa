package com.upsjb.movilsantarosa.feature.fine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FineModel(
    val id: String = "",
    val memberEmail: String = "",
    val memberName: String = "",
    val memberDniNumber: String = "",
    val reason: FineReason = FineReason.OTHER,
    val customReason: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val issuedAt: Long = 0L,
    val status: FineStatus = FineStatus.PENDING,
    val createdBy: String = "",
    val createdAt: Long = 0L
)

@Serializable
enum class FineReason {
    LATE_PAYMENT,
    ABSENCE_MEETING,
    ABSENCE_ACTIVITY,
    MISCONDUCT,
    DOCUMENTATION,
    OTHER;

    val displayName: String
        get() = when (this) {
            LATE_PAYMENT -> "Pago fuera de fecha"
            ABSENCE_MEETING -> "Inasistencia a reunión"
            ABSENCE_ACTIVITY -> "Inasistencia a actividad"
            MISCONDUCT -> "Incumplimiento del reglamento"
            DOCUMENTATION -> "Documentación incompleta"
            OTHER -> "Otro"
        }
}

@Serializable
enum class FineStatus {
    PENDING,
    PAID,
    CANCELLED;

    val displayName: String
        get() = when (this) {
            PENDING -> "Pendiente"
            PAID -> "Pagada"
            CANCELLED -> "Anulada"
        }
}