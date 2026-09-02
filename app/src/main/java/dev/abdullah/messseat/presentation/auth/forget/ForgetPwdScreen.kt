package dev.abdullah.messseat.presentation.auth.forget

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.presentation.auth.AuthEvent
import dev.abdullah.messseat.presentation.auth.forget.components.EmailSection
import dev.abdullah.messseat.presentation.auth.forget.components.PwdSection
import dev.abdullah.messseat.presentation.auth.signup.components.BottomActionBar
import dev.abdullah.messseat.presentation.auth.signup.components.SignupTopBar

@Composable
fun ForgetPwdScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    viewModel: ForgetPwdViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                AuthEvent.GoLogin -> onLogin()
                is AuthEvent.Toast -> snackbar.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )

                else -> {}
            }
        }
    }

    BackHandler(
        enabled = state.currentStep != ForgetPwdStep.EMAIL,
        onBack = viewModel::onGoBack
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            SignupTopBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
            )
        },
        bottomBar = {
            BottomActionBar(
                primaryBtnText = if (state.currentStep == ForgetPwdStep.EMAIL) "Send Verification Code" else "Reset Password",
                isLoading = state.isLoading,
                enabled = state.email.isNotEmpty(),
                suggestBtnHintText = "Remember your password? ",
                showSuggestBtnHint = state.currentStep == ForgetPwdStep.EMAIL,
                showBackButton = state.currentStep != ForgetPwdStep.EMAIL,
                onPrimaryClick = viewModel::onGoNext,
                onBackClick = viewModel::onGoBack,
                onSuggestBtnClick = onLogin
            )
        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = state.currentStep, transitionSpec = {
                val forward = state.isNavForward
                val enterDirection = if (forward) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
                val exitDirection = if (forward) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
                (slideIntoContainer(enterDirection, animationSpec = tween(280)))
                    .togetherWith(slideOutOfContainer(exitDirection, animationSpec = tween(280)))
            },
            label = "signupStepTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) { step ->

            when (step) {
                ForgetPwdStep.EMAIL -> EmailSection(
                    state = state,
                    onEmailChange = viewModel::onEmailChange
                )

                ForgetPwdStep.PASSWORD -> PwdSection(
                    state = state,
                    onChangeEmail = viewModel::changeEmail,
                    onOtpChange = viewModel::onOtpChange,
                    onPasswordChange = viewModel::onPasswordChange
                )
            }

        }

    }
}