package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.result.LocalResultEventBus
import androidx.navigation3.runtime.result.ResultEffect
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.AnnoucementsDestination
import com.upsjb.movilsantarosa.core.navigation.component.BottomSheetSceneStrategy
import com.upsjb.movilsantarosa.core.navigation.component.FineFormDestination
import com.upsjb.movilsantarosa.core.navigation.component.FinePickerDestination
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.HomeDestination
import com.upsjb.movilsantarosa.core.navigation.component.MAIN_ROUTES
import com.upsjb.movilsantarosa.core.navigation.component.MemberPickerDestination
import com.upsjb.movilsantarosa.core.navigation.component.MembersDestination
import com.upsjb.movilsantarosa.core.navigation.component.Navigator
import com.upsjb.movilsantarosa.core.navigation.component.PaymentFormDestination
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination
import com.upsjb.movilsantarosa.core.navigation.component.rememberNavigationState
import com.upsjb.movilsantarosa.core.navigation.results.FineSavedResult
import com.upsjb.movilsantarosa.core.navigation.results.PaymentSavedResult
import com.upsjb.movilsantarosa.core.uicomponents.AppBottomBar
import com.upsjb.movilsantarosa.core.uicomponents.AppFloatingActionButton
import com.upsjb.movilsantarosa.core.uicomponents.AppTopBar
import com.upsjb.movilsantarosa.feature.announcements.AnnouncementsScreen
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FineViewModel
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FinesScreen
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormMode
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormScreen
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormViewModel
import com.upsjb.movilsantarosa.feature.fine.ui.fine_picker.FinePickerBottomSheet
import com.upsjb.movilsantarosa.feature.home.ui.HomeScreen
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.ui.member_picker.MemberPickerBottomSheet
import com.upsjb.movilsantarosa.feature.members.ui.members.MembersScreen
import com.upsjb.movilsantarosa.feature.payments.ui.payment.PaymentViewModel
import com.upsjb.movilsantarosa.feature.payments.ui.payment.PaymentsScreen
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormMode
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormScreen
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(
    userRole: UserRole,
    onLogout: () -> Unit
) {
    val navigationState = rememberNavigationState(
        startRoute = HomeDestination,
        topLevelRoutes = MAIN_ROUTES
    )

    val bottomSheetStrategy = remember {
        BottomSheetSceneStrategy<NavKey>()
    }

    val navigator = remember {
        Navigator(navigationState)
    }

    val entryProvider = entryProvider {
        entry<HomeDestination> {
            HomeScreen()
        }

        entry<MembersDestination> {
            MembersScreen()
        }

        entry<MemberPickerDestination>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) {
            val resultBus = LocalResultEventBus.current
            MemberPickerBottomSheet(
                onMemberSelected = { member ->
                    resultBus.sendResult(result = member)
                    navigator.goBack()
                },
                onDismiss = { navigator.goBack() },
            )
        }

        entry<FinesDestination> {
            val viewModel: FineViewModel = hiltViewModel()

            ResultEffect<FineSavedResult> {
                viewModel.loadFines()
            }

            FinesScreen(
                onFineClick = { fine ->
                    navigator.navigate(
                        FineFormDestination(fine.id)
                    )
                }
            )
        }

        entry<FineFormDestination> { destination ->
            val viewModel: FineFormViewModel = hiltViewModel()

            ResultEffect<Member> { member ->
                viewModel.selectMember(member)
            }
            val resultBus = LocalResultEventBus.current

            LaunchedEffect(destination.fineId) {

                if (destination.fineId == null) {
                    viewModel.setMode(FineFormMode.CREATE)
                } else {
                    viewModel.loadFine(destination.fineId)
                }
            }

            FineFormScreen(
                viewModel = viewModel,
                onBackClick = {
                    navigator.goBack()
                },
                onSuccess = {
                    resultBus.sendResult(result = FineSavedResult)
                    navigator.goBack()
                },
                openMemberPicker = {
                    navigator.navigate(MemberPickerDestination)
                }
            )
        }

        entry<FinePickerDestination>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) { destination ->
            val resultBus = LocalResultEventBus.current
            FinePickerBottomSheet(
                memberEmail = destination.memberEmail,
                onFineSelected = { fine ->
                    resultBus.sendResult(result = fine)
                    navigator.goBack()
                },
                onDismiss = { navigator.goBack() },
            )
        }

        entry<PaymentsDestination> {
            val viewModel: PaymentViewModel = hiltViewModel()

            ResultEffect<PaymentSavedResult> {
                viewModel.loadPayments()
            }

            PaymentsScreen(
                onPaymentClick = { payment ->
                    navigator.navigate(
                        PaymentFormDestination(payment.id)
                    )
                }
            )
        }
        entry<PaymentFormDestination> { destination ->
            val viewModel: PaymentFormViewModel = hiltViewModel()

            ResultEffect<Member> { member ->
                viewModel.selectMember(member)
            }

            ResultEffect<Fine> { fine ->
                viewModel.selectFine(fine)
            }

            val resultBus = LocalResultEventBus.current

            LaunchedEffect(destination.paymentId) {
                if (destination.paymentId == null) {
                    viewModel.setMode(PaymentFormMode.CREATE)
                } else {
                    viewModel.loadPayment(destination.paymentId)
                }
            }

            PaymentFormScreen(
                viewModel = viewModel,
                onBackClick = {
                    navigator.goBack()
                },
                onSuccess = {
                    resultBus.sendResult(result = PaymentSavedResult)
                    navigator.goBack()
                },
                openMemberPicker = {
                    navigator.navigate(MemberPickerDestination)
                },
                openFinePicker = { memberEmail ->
                    navigator.navigate(FinePickerDestination(memberEmail))
                }
            )
        }

        entry<AnnoucementsDestination> {
            AnnouncementsScreen()
        }
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            AppTopBar(
                currentDestination = navigationState.topLevelRoute,
                onLogout = onLogout
            )
        },
        bottomBar = {
            AppBottomBar(
                currentDestination = navigationState.topLevelRoute,
                onDestinationSelected = navigator::navigate
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                currentDestination = navigationState.currentDestination,
                userRole = userRole,
                onClick = {
                    when (navigationState.topLevelRoute) {
                        FinesDestination -> {
                            navigator.navigate(FineFormDestination())
                        }

                        PaymentsDestination -> {
                            navigator.navigate(PaymentFormDestination())
                        }

                        AnnoucementsDestination -> {
                        }

                        else -> Unit
                    }
                }
            )
        }
    ) { padding ->
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = {
                navigator.goBack()
            },
            sceneStrategies = listOf(
                bottomSheetStrategy
            ),
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}