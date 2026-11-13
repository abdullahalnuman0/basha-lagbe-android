package dev.example.messseat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.example.messseat.features.auth.forget.ForgetPwdScreen
import dev.example.messseat.features.auth.otp.OtpScreen
import dev.example.messseat.features.auth.signin.SigningScreen
import dev.example.messseat.features.auth.signup.SignupScreen

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