
package com.upsjb.movilsantarosa.domain.models

data class Member(
    val id: String,
    val fullName: String,
    val dni: String,
    val isActive: Boolean = true,
    val phone: String = "",
    val email: String = ""
)

enum class MemberTab {
    HOME,
    MEMBERS,
    PAYMENTS,
    FINES,
    ANNOUNCEMENTS
}