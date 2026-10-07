package dev.abdullah.messseat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.abdullah.messseat.core.presentation.components.LocalSnackbar
import dev.abdullah.messseat.core.presentation.components.SnackbarController
import dev.abdullah.messseat.data.enums.Role
import dev.abdullah.messseat.domain.enums.ThemeMode
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.navigation.AuthNavHost
import dev.abdullah.messseat.navigation.OwnerNavHost
import dev.abdullah.messseat.navigation.TenantNavHost
import dev.abdullah.messseat.ui.theme.DarkExtended
import dev.abdullah.messseat.ui.theme.LightExtended
import dev.abdullah.messseat.ui.theme.LocalExtendedColors
import dev.abdullah.messseat.ui.theme.MessSeatTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition { !viewModel.ready }


        setContent {

            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val dark = when (themeMode) {
                ThemeMode.SYSTEM -> systemDark
                else -> themeMode == ThemeMode.DARK
            }


            LaunchedEffect(dark) {
                enableEdgeToEdge(
                    statusBarStyle = if (dark) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    },
                    navigationBarStyle = if (dark) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    },
                )
            }

            MessSeatTheme (dark){
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                val loggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
                val user by viewModel.user.collectAsStateWithLifecycle()


                CompositionLocalProvider(
                    LocalSnackbar provides SnackbarController(snackbarHostState, scope),
                    LocalExtendedColors provides if (dark) DarkExtended else LightExtended
                ) {
                    Box(Modifier.fillMaxSize()) {
                        RootNavGraph(
                            loggedIn = loggedIn,
                            loggedOut = {
                                viewModel.loggedOut {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Successfully Logged Out.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            },
                            user = user
                        )

                        SnackbarHost(
                            snackbarHostState, Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                        )
                    }
                }

            }
        }
    }

}

@Composable
fun RootNavGraph(
    loggedIn: Boolean,
    loggedOut: () -> Unit,
    user: User?
) {
    if (loggedIn)
        user?.let {
            when {
                it.roles.contains(Role.OWNER) -> OwnerNavHost(user = user, loggedOut = loggedOut)
                it.roles.contains(Role.TENANT) -> TenantNavHost(user = user, loggedOut = loggedOut)
            }
        }
    else AuthNavHost()
}
