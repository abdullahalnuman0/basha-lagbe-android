package dev.example.messseat.features.auth.forget.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.example.messseat.R
import dev.example.messseat.core.presentation.components.AuthInputTextField
import dev.example.messseat.features.auth.forget.ForgetPwdUiState
import dev.example.messseat.core.presentation.widgets.PageHeader

@Composable
internal fun EmailSection(
    modifier: Modifier = Modifier,
    state: ForgetPwdUiState,
    onEmailChange: (String) -> Unit
) {

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(48.dp))

        PageHeader(
            title = "Forget Password",
            subtitle = "Enter the email address linked to yur account and we'll send you a verification code"
        )
        Spacer(modifier = Modifier.height(24.dp))

        AuthInputTextField(
            value = state.email,
            error = state.emailError,
            onValueChange = onEmailChange,
            label = "Email Address",
            placeholder = "Enter your email address",
            icon = painterResource(R.drawable.solar_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )


    }

}