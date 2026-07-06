package com.upsjb.movilsantarosa.feature.post.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String = ""
)