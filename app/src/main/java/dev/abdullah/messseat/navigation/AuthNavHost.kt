package dev.abdullah.messseat.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.abdullah.messseat.presentation.auth.forget.ForgetPwdScreen
import dev.abdullah.messseat.presentation.auth.otp.OtpScreen
import dev.abdullah.messseat.presentation.auth.signin.SigningScreen
import dev.abdullah.messseat.presentation.auth.signup.SignupScreen

@Composable
fun AuthNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = AuthRoute.Login,
        // --- animation ---
        enterTransition = enterPush(),
        exitTransition = exitPush(),
        popEnterTransition = enterPop(),
        popExitTransition = exitPop()
    ) {

        composable<AuthRoute.Login> {
            SigningScreen(
                onVerify = { navController.navigate(AuthRoute.OtpVerify(it)) },
                onSignup = { navController.navigate(AuthRoute.Signup) },
                onForgetPwd = { navController.navigate(AuthRoute.ForgetPwd) }
            )
        }

        composable<AuthRoute.Signup> {
            SignupScreen(
                onLogin = { navController.popBackStack() },
                onVerify = {
                    navController.navigate(AuthRoute.OtpVerify(it)) {
                        popUpTo<AuthRoute.Signup> { inclusive = true }
                    }
                }
            )
        }

        composable<AuthRoute.OtpVerify> {
            val toRoute = it.toRoute<AuthRoute.OtpVerify>()

            OtpScreen(
                email = toRoute.email,
                recover = toRoute.isRecovery,
                onBack = { navController.popBackStack() }
            )
        }

        composable<AuthRoute.ForgetPwd> {
            ForgetPwdScreen(
                onLogin = { navController.popBackStack() }
            )
        }

    }

}