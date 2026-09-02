package dev.abdullah.messseat.presentation.auth.signup

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
import dev.abdullah.messseat.presentation.auth.signup.components.AboutYouSection
import dev.abdullah.messseat.presentation.auth.signup.components.BasicInfoSection
import dev.abdullah.messseat.presentation.auth.signup.components.BottomActionBar
import dev.abdullah.messseat.presentation.auth.signup.components.LocationSection
import dev.abdullah.messseat.presentation.auth.signup.components.RoleSelectionSection
import dev.abdullah.messseat.presentation.auth.signup.components.SecuritySection
import dev.abdullah.messseat.presentation.auth.signup.components.SignupTopBar
import androidx.compose.material3.MaterialTheme.colorScheme as color


@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onVerify: (String) -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {

                is AuthEvent.GoVerify -> onVerify(it.email)

                is AuthEvent.Toast -> {
                    snackbarHostState.showSnackbar(
                        it.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }

                else -> onLogin()
            }
        }
    }

    BackHandler(
        enabled = state.currentStep != SignupStep.ROLE_SELECTION,
        onBack = viewModel::onGoBack
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SignupTopBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),

                currentStep = state.currentStep,
                userRole = state.selectedRole,
                onBackClick = viewModel::onGoBack
            )
        },
        bottomBar = {
            BottomActionBar(
                primaryBtnText = if (state.currentStep == SignupStep.SECURITY) "Create Account" else "Continue",
                enabled = state.selectedRole != null,
                isLoading = state.isLoading,
                showSuggestBtnHint = state.currentStep == SignupStep.ROLE_SELECTION,
                showBackButton = state.currentStep != SignupStep.ROLE_SELECTION,
                onPrimaryClick = viewModel::onGoNext,
                onBackClick = viewModel::onGoBack,
                onSuggestBtnClick = onLogin
            )
        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = state.currentStep, transitionSpec = {
                val forward = state.isNavigatingForward
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
                SignupStep.ROLE_SELECTION -> RoleSelectionSection(
                    userRole = state.selectedRole,
                    onRoleSelected = viewModel::onRoleSelected,
                )

                SignupStep.BASIC_INFO -> BasicInfoSection(
                    state = state,
                    onFullNameChange = viewModel::onFullNameChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPhoneChange = viewModel::onPhoneChange
                )

                SignupStep.ABOUT_YOU -> AboutYouSection(
                    state = state,
                    onGenderSelected = viewModel::onGenderSelected,
                    onInstitutionChange = viewModel::onInstitutionChange,
                    onInstitutionNotListedToggle = viewModel::onInstitutionNotListedToggle
                )

                SignupStep.LOCATION -> LocationSection(
                    state = state,
                    onDivisionSelected = viewModel::onDivisionSelected,
                    onDistrictSelected = viewModel::onDistrictSelected,
                    onAreaSelected = viewModel::onAreaSelected
                )

                SignupStep.SECURITY -> SecuritySection(
                    state = state,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPassChange = viewModel::onConfirmPassChange,
                    onTermsToggle = viewModel::onTermsToggle
                )

            }
        }

    }


}