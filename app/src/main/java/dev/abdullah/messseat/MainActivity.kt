package dev.abdullah.messseat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.data.enums.UserStatus
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.navigation.AuthNavHost
import dev.abdullah.messseat.presentation.auth.signup.SignupScreen
import dev.abdullah.messseat.ui.theme.MessSeatTheme
import kotlin.math.log


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition { !viewModel.ready }

        enableEdgeToEdge()

        setContent {
            MessSeatTheme {

                val loggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
                val user by viewModel.user.collectAsStateWithLifecycle()

                RootNavGraph(
                    loggedIn = loggedIn,
                    loggedOut = viewModel::loggedOut,
                    user = user
                )
            }
        }
    }
}

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    loggedIn: Boolean,
    loggedOut: () -> Unit,
    user: User?
) {
    if (loggedIn) {
        Box(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            user?.let {
                UsrCard(usr = user, loggedOut = loggedOut)
            }
        }
    } else AuthNavHost()
}

@Composable
fun UsrCard(
    modifier: Modifier = Modifier,
    usr: User,
    loggedOut: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                if (!usr.avatarUrl.isNullOrBlank()) {
//                    AsyncImage(
//                        model = usr.avatarUrl,
//                        contentDescription = "Profile picture",
//                        modifier = Modifier
//                            .size(76.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop
//                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usr.fullName
                                .trim()
                                .split(" ")
                                .take(2)
                                .joinToString("") { it.firstOrNull()?.uppercase() ?: "" },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer( Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = usr.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = usr.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status
                Surface(
                    shape = RoundedCornerShape(50),
                    color = when (usr.status) {
                        UserStatus.ACTIVE ->
                            MaterialTheme.colorScheme.primaryContainer

                        else ->
                            MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = usr.status.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            // User information
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                UserInfoRow(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    value = usr.email
                )

                UserInfoRow(
                    icon = Icons.Outlined.Phone,
                    label = "Phone",
                    value = usr.phone.ifBlank { "Not provided" }
                )

                UserInfoRow(
                    icon = Icons.Outlined.Person,
                    label = "Gender",
                    value = usr.gender.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                )

                UserInfoRow(
                    icon = Icons.Outlined.School,
                    label = "Institution",
                    value = usr.institution ?: "Not provided"
                )

                UserInfoRow(
                    icon = Icons.Outlined.LocationOn,
                    label = "Location",
                    value = listOfNotNull(
                        usr.area,
                        usr.district,
                        usr.division
                    ).joinToString(", ").ifBlank {
                        "Not provided"
                    }
                )

                UserInfoRow(
                    icon = Icons.Outlined.Badge,
                    label = "Roles",
                    value = usr.roles.joinToString(", ") {
                        it.toString()
                            .lowercase()
                            .replaceFirstChar { c -> c.uppercase() }
                    }.ifBlank {
                        "User"
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email verification
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (usr.emailVerified) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (usr.emailVerified) {
                            Icons.Outlined.Verified
                        } else {
                            Icons.Outlined.Warning
                        },
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = if (usr.emailVerified) {
                            "Email verified"
                        } else {
                            "Email not verified"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout
            OutlinedButton(
                onClick = loggedOut,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Log out",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Composable
private fun UserInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}