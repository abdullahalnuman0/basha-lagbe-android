package dev.abdullah.messseat.presentation.auth.otp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.R
import dev.abdullah.messseat.presentation.auth.AuthEvent
import androidx.compose.material3.MaterialTheme.colorScheme as color
import androidx.compose.material3.MaterialTheme.typography as typo

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    email: String,
    recover: Boolean,
    onBack: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {

        if (state.resendCount < 1)
            viewModel.resend(email, recover)

        viewModel.events.collect {
            when (it) {
                is AuthEvent.Toast -> {
                    snackbarHostState.showSnackbar(
                        it.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }

                else -> {}
            }
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color.background)
            .padding(24.dp), contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(
            snackbarHostState,
            Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = color.surface,
                contentColor = color.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                HeaderLogo()

                HeaderText(
                    title = if (recover) "Restore your account" else "Verify your account",
                    subTitle = "Enter the 6-diggit code sent to (${email.take(2)}...@${
                        email.substringAfter(
                            '@'
                        )
                    }). Please check your inbox or spam folder.",
                )

                OtpInputField(onOtpChange = viewModel::onOtpChange)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.otp.length == 6 && !state.isLoading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.verifyOtp(email, recover)
                    }
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
                                text = if (isLoading) {
                                    if (state.isSending) "Sending..." else "Verifying..."
                                } else {
                                    if (recover) "Restore account" else "Verify OTP"
                                }
                            )
                            if (isLoading) CircularProgressIndicator(Modifier.size(24.dp))
                        }

                    }
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Don't receive code?",
                        style = typo.labelMedium
                    )

                    Text(
                        text = if (state.isResendCount) "Resend in ${
                            "%d:%02d".format(state.resendCooldown / 60, state.resendCooldown % 60)
                        }" else "Resend OTP",
                        style = typo.labelMedium.copy(
                            fontWeight = if (state.isResendCount) FontWeight.Normal else FontWeight.Bold,
                            color = if (state.isResendCount) LocalContentColor.current else color.primary
                        ),
                        modifier = Modifier.clickable(
                            enabled = !state.isResendCount && !state.isLoading,
                            onClick = { viewModel.resend(email, recover) }
                        )
                    )
                }
            }
        }

    }

}

@Composable
 fun OtpInputField(
    modifier: Modifier = Modifier,
    onOtpChange: (String) -> Unit
) {

    val digits = remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        repeat(6) { index ->
            OtpDigit(
                modifier = Modifier.weight(1f),
                value = digits.value[index],
                focusRequester = focusRequesters[index],
                onValueChange = { newDigit ->

                    val updated = digits.value.toMutableList()
                    updated[index] = newDigit
                    digits.value = updated

                    onOtpChange(updated.joinToString(""))

                    // --- move forward ---
                    if (newDigit.isNotEmpty() && index < 5)
                        focusRequesters[index + 1].requestFocus()

                },
                onBackspace = {
//                    if (digits.value[index].isEmpty() && index > 0) {
//
//                        val updated = digits.value.toMutableList()
//                        updated[index - 1] = ""
//                        digits.value = updated
//
//                        onOtpChange(updated.joinToString(""))
//
//                        focusRequesters[index - 1].requestFocus()
//                    }
                    val updated = digits.value.toMutableList()

                    if (updated[index].isNotEmpty()) {

                        // Current digit clear
                        updated[index] = ""

                        digits.value = updated

                        onOtpChange(
                            updated.joinToString("")
                        )

                    } else if (index > 0) {

                        // Move to previous digit
                        updated[index - 1] = ""

                        digits.value = updated

                        onOtpChange(
                            updated.joinToString("")
                        )

                        focusRequesters[index - 1].requestFocus()
                    }
                }
            )
        }

    }

}

@Composable
private fun OtpDigit(
    modifier: Modifier = Modifier,
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit
) {

    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = { newDigit ->
            onValueChange(
                newDigit.filter { it.isDigit() }
                    .takeLast(1)
            )
        },
        modifier = modifier
            .height(56.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                focused = it.isFocused
            }
            .onKeyEvent { event ->

                if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                    onBackspace()
                    true
                } else {
                    false
                }
            },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 21.sp,
            color = if (focused) color.primary else color.onSurface.copy(0.7f),
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = if (focused) 2.dp else 1.dp,
                        color = if (focused) color.primary else color.onSurface.copy(0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )

}


@Composable
fun HeaderText(
    title: String,
    subTitle: String
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = typo.headlineLarge
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subTitle,
            style = typo.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(0.5f)
        )
    }

}

@Composable
fun HeaderLogo(modifier: Modifier = Modifier) {

    val appName = stringResource(R.string.app_name)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.primary)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = color.primary.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.logo),
                contentDescription = "$appName Logo",
                tint = color.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = appName,
            style = typo.titleLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = (-0.3).sp,
            )
        )
    }
}

