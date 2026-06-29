// ui/feature/home/HomeScreen.kt
package com.upsjb.movilsantarosa.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.domain.models.HomeTab
import com.upsjb.movilsantarosa.ui.feature.home.components.HomeBottomNavigation
import com.upsjb.movilsantarosa.ui.feature.home.components.HomeHeader
import com.upsjb.movilsantarosa.ui.feature.home.components.SummaryStatsSection
import com.upsjb.movilsantarosa.ui.feature.home.components.WelcomeSection

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(HomeTab.HOME) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header fijo en la parte superior
        HomeHeader()

        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeSection(
                userName = "Jorge",
                userRole = "Presidente"
            )

            SummaryStatsSection(
                activeMembers = 20,
                debtors = 5,
                paymentsOnTime = 15,
                pendingFines = 3
            )

            // Aquí iría el contenido dinámico según la pestaña seleccionada
            when (selectedTab) {
                HomeTab.HOME -> {
                    // Ya mostramos el contenido principal
                }
                HomeTab.MEMBERS -> {
                    // Contenido de socios
                }
                HomeTab.PAYMENTS -> {
                    // Contenido de pagos
                }
                HomeTab.FINES -> {
                    // Contenido de multas
                }
                HomeTab.ANNOUNCEMENTS -> {
                    // Contenido de anuncios
                }
            }

            // Espacio adicional al final
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5))
            )
        }

        // Bottom Navigation fijo en la parte inferior
        HomeBottomNavigation(
            currentTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Full")
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

@Preview(showBackground = true, name = "Home Screen - Members Tab")
@Composable
fun PreviewHomeScreenMembers() {
    HomeScreen()
    // Nota: En la preview no podemos cambiar el estado fácilmente,
    // pero en la implementación real se cambiaría con onTabSelected
}

@Preview(showBackground = true, name = "Home Screen - Small Content")
@Composable
fun PreviewHomeScreenSmallContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        HomeHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            WelcomeSection(
                userName = "Jorge",
                userRole = "Presidente"
            )

            SummaryStatsSection(
                activeMembers = 20,
                debtors = 5,
                paymentsOnTime = 15,
                pendingFines = 3
            )
        }

        HomeBottomNavigation(currentTab = HomeTab.HOME)
    }
}