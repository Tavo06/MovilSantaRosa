package com.upsjb.movilsantarosa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.data.authentic.repository.AuthRepositoryImpl
import com.upsjb.movilsantarosa.domain.authentic.usecase.LoginUseCase
import com.upsjb.movilsantarosa.ui.feature.login.LoginScreen
import com.upsjb.movilsantarosa.ui.feature.login.LoginUIState
import com.upsjb.movilsantarosa.ui.feature.login.LoginViewModel
import com.upsjb.movilsantarosa.ui.feature.login.components.ErrorDialog
import com.upsjb.movilsantarosa.ui.feature.login.components.ProgressIndicatorOverlay
import com.upsjb.movilsantarosa.ui.theme.MovilSantaRosaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MovilSantaRosaTheme {

                // 🔥 Firebase instances
                val auth = FirebaseAuth.getInstance()
                val database = FirebaseDatabase.getInstance()

                // 🔥 Repository
                val authRepository = AuthRepositoryImpl(auth, database)

                // 🔥 UseCase
                val loginUseCase = LoginUseCase(authRepository)

                val viewModel: LoginViewModel = viewModel {
                    LoginViewModel(loginUseCase)
                }
                val uiState by viewModel.uiState.collectAsState()

                Box(modifier = Modifier.fillMaxSize()) {

                    LoginScreen(
                        uiState = uiState,
                        onLoginClick = { email, password ->
                            viewModel.login(email, password)
                        },
                        onRegisterClick = {
                            // navegar a registro
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    when (uiState) {
                        is LoginUIState.Loading -> {
                            ProgressIndicatorOverlay()
                        }

                        is LoginUIState.Error -> {
                            ErrorDialog(
                                message = (uiState as LoginUIState.Error).message
                            ) { viewModel.reset() }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

