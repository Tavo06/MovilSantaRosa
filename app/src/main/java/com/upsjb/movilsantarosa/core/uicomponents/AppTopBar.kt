package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.HomeDestination
import com.upsjb.movilsantarosa.core.navigation.component.MembersDestination
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination
import com.upsjb.movilsantarosa.core.navigation.component.PostDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentDestination: NavKey,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentDestination) {

        HomeDestination -> {
            HomeHeader(
                onMenuClick = onMenuClick,
                modifier = modifier
            )
        }

        MembersDestination -> {
            TopBarHeader(
                title = "Socios",
                onMenuClick = onMenuClick,
                modifier = modifier
            )
        }

        FinesDestination -> {
            TopBarHeader(
                title = "Multas",
                onMenuClick = onMenuClick,
                modifier = modifier
            )
        }

        PaymentsDestination -> {
            TopBarHeader(
                title = "Pagos",
                onMenuClick = onMenuClick,
                modifier = modifier
            )
        }

        PostDestination -> {
            TopBarHeader(
                title = "Anuncios",
                onMenuClick = onMenuClick,
                modifier = modifier
            )
        }
    }
}