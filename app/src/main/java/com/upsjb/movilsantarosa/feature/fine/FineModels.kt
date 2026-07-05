package com.upsjb.movilsantarosa.feature.fine

data class Fine(
    val id: String,
    val memberName: String,
    val reason: String,
    val date: String,
    val amount: Double
)

enum class FineTab {
    HOME,
    MEMBERS,
    PAYMENTS,
    FINES,
    ANNOUNCEMENTS
}