package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.presentation.auth.signup.SignupUiState

@Composable
internal fun BasicInfoSection(
    modifier: Modifier = Modifier,
    state: SignupUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {

        Spacer(modifier = Modifier.height(4.dp))

        PageHeader(
            title = "Let's get started",
            subtitle = "Tell us a little about yourself to create your account."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---Field 1: full name ---
        AuthInputTextField(
            value = state.fullName,
            error = state.fullNameError,
            onValueChange = onFullNameChange,
            label = "Full Name",
            placeholder = "Enter your full name",
            icon = painterResource(R.drawable.solar_user)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthInputTextField(
            value = state.email,
            error = state.emailError,
            onValueChange = onEmailChange,
            label = "Email Address",
            placeholder = "Enter your email address",
            icon = painterResource(R.drawable.solar_email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthInputTextField(
            value = state.phone,
            error = state.phoneError,
            onValueChange = onPhoneChange,
            label = "Phone Number",
            placeholder = "01XXXXXXXXX",
            icon = painterResource(R.drawable.solar_phone),
            isPhone = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )


    }

}