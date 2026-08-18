package com.upsjb.movilsantarosa.feature.home.domain.model

data class HomeStats(
    val totalMembers: Int = 0,
    val totalFines: Int = 0,
    val totalPayments: Int = 0,
    val totalPost: Int = 0,
    val pendingRegistrations: Int = 0
)