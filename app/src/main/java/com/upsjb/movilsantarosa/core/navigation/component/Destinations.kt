package com.upsjb.movilsantarosa.core.navigation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssistantPhoto
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TaxiAlert
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
data object MemberPickerDestination : NavKey

@Serializable
data object FinesDestination : NavKey

@Serializable
data class FineFormDestination(val fineId: String? = null) : NavKey

@Serializable
data class FinePickerDestination(val memberEmail: String) : NavKey

@Serializable
data object PaymentsDestination : NavKey

@Serializable
data class PaymentFormDestination(val paymentId: String? = null) : NavKey

@Serializable
data object PostDestination : NavKey
@Serializable
data class PostFormDestination(val postId: String? = null) : NavKey

@Serializable
data class PickerMapDestination(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val readOnly: Boolean = false
) : NavKey

@Serializable
data object PendingRegistrationsDestination : NavKey

val BOTTOM_BAR_ITEMS = mapOf(
    HomeDestination to NavBarItem(Icons.Default.Home, "Home"),
    MembersDestination to NavBarItem(Icons.Default.Person, "Socios"),
    FinesDestination to NavBarItem(Icons.Default.AssistantPhoto, "Multas"),
    PaymentsDestination to NavBarItem(Icons.Default.Payments, "Pagos"),
    PostDestination to NavBarItem(Icons.Default.TaxiAlert, "Anuncios"),
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
    PostDestination
)

val TOP_LEVEL_ROUTES = AUTH_ROUTES + MAIN_ROUTES

class NavBarItem(
    val icon: ImageVector,
    val description: String
)