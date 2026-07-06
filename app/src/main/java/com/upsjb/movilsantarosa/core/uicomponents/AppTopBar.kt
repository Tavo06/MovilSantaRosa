package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.upsjb.movilsantarosa.core.navigation.component.AnnoucementsDestination
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.HomeDestination
import com.upsjb.movilsantarosa.core.navigation.component.MembersDestination
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentDestination: NavKey,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentDestination) {

        HomeDestination -> {
            HomeHeader(
                onLogout = onLogout,
                modifier = modifier
            )
        }

        MembersDestination -> {
            TopBarHeader("Socios", onLogout = onLogout,)
        }

        FinesDestination -> {
            TopBarHeader("Multas", onLogout = onLogout,)
        }

        PaymentsDestination -> {
            TopBarHeader("Pagos", onLogout = onLogout,)
        }

        AnnoucementsDestination -> {
            TopBarHeader("Anuncios", onLogout = onLogout,)
        }
    }
}