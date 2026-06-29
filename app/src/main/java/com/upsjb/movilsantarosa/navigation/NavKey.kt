package com.upsjb.movilsantarosa.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination : NavKey
@Serializable
data object RegisterDestination : NavKey

@Serializable
data object HomeDestination : NavKey

@Serializable
data object MembersDestination : NavKey

@Serializable
data object PaymentsDestination : NavKey

@Serializable
data object FinesDestination : NavKey

@Serializable
data object AnnouncementsDestination : NavKey