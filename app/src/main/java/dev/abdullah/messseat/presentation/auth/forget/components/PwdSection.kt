package dev.abdullah.messseat.presentation.auth.forget.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.colorScheme as color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.R
import dev.abdullah.messseat.presentation.auth.AuthInputTextField
import dev.abdullah.messseat.presentation.auth.forget.ForgetPwdUiState
import dev.abdullah.messseat.presentation.auth.otp.OtpInputField
import dev.abdullah.messseat.presentation.auth.signup.components.PasswordRequirementCard
import dev.abdullah.messseat.presentation.auth.signup.components.PasswordStrengthCard
import dev.abdullah.messseat.presentation.auth.signup.components.StepHeader

@Composable
internal fun PwdSection(
    modifier: Modifier = Modifier,
    state: ForgetPwdUiState,
    onChangeEmail: () -> Unit,
    onOtpChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {

    Column(
        modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(48.dp))

        StepHeader(
            title = "Verify & Set Your Password",
            subtitle = "Enter the 6-digit code sent to your email and create a strong password for your account."
        )

        Spacer(modifier = Modifier.height(24.dp))

        EmailCard(
            email = state.email,
            onChangeEmail = onChangeEmail
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter you OTP",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OtpInputField(modifier = Modifier.padding(horizontal = 16.dp), onOtpChange = onOtpChange)

        Spacer(modifier = Modifier.height(16.dp))

        AuthInputTextField(
            value = state.password,
            error = state.passwordError,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Create a password",
            isPassword = true,
            icon = painterResource(R.drawable.solar_lock_keyhole),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        AnimatedVisibility(state.password.isNotEmpty()) {
            PasswordStrengthCard(strength = state.strength)
        }

        Spacer(Modifier.height(12.dp))

        PasswordRequirementCard(
            passwordRequirements = state.passwordRequirements
        )


    }
}

@Composable
private fun EmailCard(
    modifier: Modifier = Modifier,
    email: String,
    onChangeEmail: () -> Unit
) {
    Card(
        modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = lerp(color.surface, color.primary, 0.1f),
            contentColor = color.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.solar_email),
                    contentDescription = "Email",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = email.take(1),
                    style = MaterialTheme.typography.labelLarge
                )
                repeat(5) {
                    Box(
                        Modifier
                            .size(4.dp)
                            .background(color.onSurface, CircleShape)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(2.dp))
                }
                Text(
                    text = "@${email.substringAfter('@')}",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Box(
                Modifier
                    .size(4.dp)
                    .background(color.onSurface, CircleShape)
                    .clip(CircleShape)
            )

            Text(
                text = "Change email",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color.primary
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onChangeEmail)
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            )

        }
    }
}