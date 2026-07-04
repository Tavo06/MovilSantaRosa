package com.upsjb.movilsantarosa.core.navigation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
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
data object FinesDestination : NavKey

@Serializable
data object PaymentsDestination : NavKey

@Serializable
data object AnnoucementsDestination : NavKey

val BOTTOM_BAR_ITEMS = mapOf(
    HomeDestination to NavBarItem(Icons.Default.Home, "Home"),
    MembersDestination to NavBarItem(Icons.Default.Face, "Miembros"),
    FinesDestination to NavBarItem(Icons.Default.Camera, "Multas"),
    PaymentsDestination to NavBarItem(Icons.Default.Camera, "Pagos"),
    AnnoucementsDestination to NavBarItem(Icons.Default.Camera, "Anuncios"),
)

val AUTH_ROUTES = setOf(
    LoginDestination,
    RegisterDestination
)

val MAIN_ROUTES = setOf(
    HomeDestination,
    MembersDestination,
    FinesDestination,
    PaymentsDestination,
    AnnoucementsDestination
)

val TOP_LEVEL_ROUTES = AUTH_ROUTES + MAIN_ROUTES
class NavBarItem(
    val icon: ImageVector,
    val description: String
)