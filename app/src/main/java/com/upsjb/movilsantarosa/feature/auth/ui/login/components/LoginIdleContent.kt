package com.upsjb.movilsantarosa.feature.auth.ui.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.core.uicomponents.AppOutlinedButton
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField

@Composable
fun LoginIdleContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {

            LoginHeader()

            LoginForm(
                email = email,
                password = password,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick
            )
        }

        LoginBottomDecoration(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.logo_app),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "Santa Rosa de Lima",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "San Clemente - Pisco",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = "Honradez, Seguridad y Confianza",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        FormTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            placeholder = "correo@ejemplo.com",
            singleLine = true,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        FormTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            placeholder = "••••••••",
            singleLine = true,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        AppPrimaryButton(
            text = "Ingresar",
            enabled = email.isNotBlank() && password.isNotBlank(),
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AppOutlinedButton(
            text = "Registrarse",
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun LoginBottomDecoration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginHeaderPreview() {
    LoginHeader()
}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    LoginForm(
        email = "",
        password = "",
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginFormFilledPreview() {
    LoginForm(
        email = "correo@ejemplo.com",
        password = "123456",
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginIdleContentPreview() {
    LoginIdleContent(
        email = "",
        password = "",
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onRegisterClick = {}
    )
}