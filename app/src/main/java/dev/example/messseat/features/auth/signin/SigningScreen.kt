package dev.example.messseat.features.auth.signin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.R
import dev.example.messseat.features.auth.AuthEvent
import dev.example.messseat.core.presentation.components.AuthInputTextField
import dev.example.messseat.features.auth.otp.HeaderLogo
import dev.example.messseat.core.presentation.widgets.PageHeader
import dev.example.messseat.features.shared.settings.components.SectionCard
import androidx.compose.material3.MaterialTheme.colorScheme as color


@Composable
fun SigningScreen(
    modifier: Modifier = Modifier,
    viewModel: SigningViewModel = hiltViewModel(),
    onVerify: (String) -> Unit,
    onSignup: () -> Unit,
    onForgetPwd: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is AuthEvent.GoVerify -> onVerify(it.email)
                else -> {}
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = color.primary
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(onClick = onSignup)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                )

            }
        }
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(48.dp))

            HeaderLogo()

            PageHeader(
                title = "Welcome Back",
                subtitle = "Login to find or rent your perfect home or mess",
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            SectionCard(contentPadding = PaddingValues(0.dp)) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // --- Error dialog ---
                    AnimatedVisibility(visible = state.error != null) {
                        Text(
                            text = state.error ?: "Something went wrong!!",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = color.onError
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = color.error,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }


                    AuthInputTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Email Address",
                        placeholder = "Enter your email address",
                        icon = painterResource(R.drawable.solar_email)
                    )

                    AuthInputTextField(
                        value = state.password,
                        onValueChange = viewModel::onPdwChange,
                        label = "Password",
                        placeholder = "Enter your password",
                        isPassword = true,
                        icon = painterResource(R.drawable.solar_lock_keyhole),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forget Password?",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = color.primary
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onForgetPwd() }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading && state.email.isNotEmpty() && state.password.isNotEmpty(),
                        onClick = viewModel::login
                    ) {
                        AnimatedContent(
                            targetState = state.isLoading,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(250)) togetherWith
                                        fadeOut(animationSpec = tween(250))
                            },
                            label = "btnText"
                        ) { isLoading ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (state.isLoading) "Logging..." else "Login"
                                )
                                if (isLoading) CircularProgressIndicator(Modifier.size(24.dp))
                                else Icon(Icons.AutoMirrored.Default.ArrowRightAlt, "Login")
                            }

                        }
                    }

                }
            }


        }

    }

}
