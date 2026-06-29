package com.upsjb.movilsantarosa.core.navigation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface BottomNavKey : NavKey {
    val icon: ImageVector
    val label: String

    @Serializable
    data object HomeDestination : BottomNavKey {
        override val icon: ImageVector = Icons.Default.Home
        override val label: String = "Home"
    }

    @Serializable
    data object MembersDestination : BottomNavKey {
        override val icon: ImageVector = Icons.Default.Person
        override val label: String = "Miembros"
    }

    @Serializable
    data object FinesDestination : BottomNavKey {
        override val icon: ImageVector = Icons.Default.Settings
        override val label: String = "Multas"
    }
    @Serializable
    data object AnnouncementsDestination : BottomNavKey {
        override val icon: ImageVector = Icons.Default.Settings
        override val label: String = "Anuncios"
    }

    companion object {
        val items = listOf(HomeDestination, MembersDestination, FinesDestination, AnnouncementsDestination)

        val stateSaver = Saver<BottomNavKey, String>(
            save = { it::class.qualifiedName },
            restore = { qualifiedClass ->
                items.firstOrNull { it::class.qualifiedName == qualifiedClass } ?: HomeDestination
            }
        )
    }
}