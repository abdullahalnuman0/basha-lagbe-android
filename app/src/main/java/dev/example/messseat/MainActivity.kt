package dev.example.messseat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.example.messseat.core.presentation.components.LocalSnackbar
import dev.example.messseat.core.presentation.components.SnackbarController
import dev.example.messseat.core.push.RequestNotificationPermission
import dev.example.messseat.data.enums.Role
import dev.example.messseat.domain.enums.ThemeMode
import dev.example.messseat.domain.model.User
import dev.example.messseat.features.shared.notification.NotificationHelper
import dev.example.messseat.navigation.AuthNavHost
import dev.example.messseat.navigation.OwnerNavHost
import dev.example.messseat.navigation.TenantNavHost
import dev.example.messseat.ui.theme.DarkExtended
import dev.example.messseat.ui.theme.LightExtended
import dev.example.messseat.ui.theme.LocalExtendedColors
import dev.example.messseat.ui.theme.MessSeatTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // A silent denial means the user chose "don't ask again"; the only way forward is settings.
            if (!granted && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                NotificationHelper.openNotificationSettings(this)
            }
            refreshSystemStatus()
        }

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

            MessSeatTheme(dark) {
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
                            user = user,
                            onRegisterPush = viewModel::onRegisterPush,
                            onRequestNotifications = ::requestNotifications
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

    override fun onResume() {
        super.onResume()
        refreshSystemStatus()
    }

    private fun refreshSystemStatus() {
        viewModel.refreshNotificationStatus(
            allowed = NotificationHelper.catPostNotifications(this)
        )
    }

    private fun requestNotifications() {
        val needsRuntimePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
        if (needsRuntimePermission) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            NotificationHelper.openNotificationSettings(this)
        }
    }
}

@Composable
fun RootNavGraph(
    loggedIn: Boolean,
    loggedOut: () -> Unit,
    user: User?,
    onRegisterPush: () -> Unit,
    onRequestNotifications: () -> Unit,
) {
    if (loggedIn)
        user?.let {
            // Ask for notification permission (Android 13+) and register this device for push.
            RequestNotificationPermission()
            LaunchedEffect(Unit) { onRegisterPush() }

            when {
                it.roles.contains(Role.OWNER) -> OwnerNavHost(
                    user = user,
                    loggedOut = loggedOut,
                    onRequestNotifications = onRequestNotifications
                )

                it.roles.contains(Role.TENANT) -> TenantNavHost(
                    user = user,
                    loggedOut = loggedOut,
                    onRequestNotifications = onRequestNotifications
                )
            }
        }
    else AuthNavHost()
}


@Composable
fun mainViewModel(): MainViewModel {
    val activity = LocalActivity.current
        ?: error("mainViewModel() must be called inside an Activity")

    return hiltViewModel(
        viewModelStoreOwner = activity as ViewModelStoreOwner
    )
}
