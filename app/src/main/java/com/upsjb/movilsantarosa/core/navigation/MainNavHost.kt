package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.result.LocalResultEventBus
import androidx.navigation3.runtime.result.ResultEffect
import androidx.navigation3.ui.NavDisplay
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
import com.upsjb.movilsantarosa.core.navigation.component.PendingRegistrationsDestination
import com.upsjb.movilsantarosa.core.navigation.component.PickerMapDestination
import com.upsjb.movilsantarosa.core.navigation.component.PostDestination
import com.upsjb.movilsantarosa.core.navigation.component.PostFormDestination
import com.upsjb.movilsantarosa.core.navigation.component.rememberNavigationState
import com.upsjb.movilsantarosa.core.uicomponents.AppFloatingActionButton
import com.upsjb.movilsantarosa.core.uicomponents.AppNavigationDrawer
import com.upsjb.movilsantarosa.core.uicomponents.AppTopBar
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FinesScreen
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormMode
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormScreen
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormViewModel
import com.upsjb.movilsantarosa.feature.fine.ui.fine_picker.FinePickerBottomSheet
import com.upsjb.movilsantarosa.feature.home.ui.HomeScreen
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.ui.member_picker.MemberPickerBottomSheet
import com.upsjb.movilsantarosa.feature.members.ui.members.MembersScreen
import com.upsjb.movilsantarosa.feature.members.ui.pending_registrations.PendingRegistrationsBottomSheet
import com.upsjb.movilsantarosa.feature.payments.ui.payment.PaymentsScreen
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormMode
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormScreen
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormViewModel
import com.upsjb.movilsantarosa.feature.post.domain.model.Location
import com.upsjb.movilsantarosa.feature.post.ui.location_picker.LocationPickerScreen
import com.upsjb.movilsantarosa.feature.post.ui.post.PostsScreen
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormMode
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormScreen
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormViewModel
import kotlinx.coroutines.launch

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val entryProvider = entryProvider {
        entry<HomeDestination> {
            HomeScreen(
                onOpenPendingRequests = {
                    navigator.navigate(PendingRegistrationsDestination)
                }
            )
        }

        entry<PendingRegistrationsDestination>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) {
            PendingRegistrationsBottomSheet(
                onDismiss = { navigator.goBack() }
            )
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

        entry<PostDestination> {
            PostsScreen(
                onPostClick = { post ->
                    navigator.navigate(
                        PostFormDestination(post.id)
                    )
                }
            )
        }
        entry<PostFormDestination> { destination ->

            val viewModel: PostFormViewModel = hiltViewModel()

            ResultEffect<Location> { location ->
                viewModel.updateForm {
                    copy(
                        latitude = location.latitude.toString(),
                        longitude = location.longitude.toString(),
                        address = location.address
                    )
                }
            }

            LaunchedEffect(destination.postId) {
                if (destination.postId == null) {
                    viewModel.setMode(PostFormMode.CREATE)
                } else {
                    viewModel.loadPost(destination.postId)
                }
            }

            PostFormScreen(
                viewModel = viewModel,
                onBackClick = {
                    navigator.goBack()
                },
                onSuccess = {
                    navigator.goBack()
                },
                openLocationPicker = {
                    navigator.navigate(PickerMapDestination)
                }
            )
        }
        entry<PickerMapDestination> {
            val resultBus = LocalResultEventBus.current

            LocationPickerScreen(
                onLocationSelected = {
                    resultBus.sendResult(result = it)
                },
                onBack = { navigator.goBack() }
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                currentDestination = navigationState.topLevelRoute,
                onDestinationSelected = { destination ->
                    navigator.navigate(destination)
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                AppTopBar(
                    currentDestination = navigationState.topLevelRoute,
                    onLogout = onLogout,
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
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

                            PostDestination -> {
                                navigator.navigate(PostFormDestination())
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
}