package dev.example.messseat.features.auth.signup.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.example.messseat.R
import dev.example.messseat.core.presentation.widgets.StepProgressIndicator
import dev.example.messseat.domain.enums.UserRole
import dev.example.messseat.features.auth.signup.SignupStep
import androidx.compose.material3.MaterialTheme.colorScheme as color
import androidx.compose.material3.MaterialTheme.typography as typo

@Composable
internal fun SignupTopBar(
    modifier: Modifier = Modifier,
    currentStep: SignupStep = SignupStep.ROLE_SELECTION,
    userRole: UserRole? = null,
    onBackClick: () -> Unit = {}
) {
    val appName = stringResource(R.string.app_name)
    val showProgress = currentStep in SignupStep.progressSteps

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AnimatedContent(
                targetState = currentStep == SignupStep.ROLE_SELECTION,
                label = "TopBarNavigation",
            ) { isRoleSelection ->

                if (isRoleSelection) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color.primary)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(12.dp),
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
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp,
                                fontSize = 21.sp
                            )
                        )
                    }
                } else {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Selected role
            AnimatedVisibility(
                visible = userRole != null && showProgress,
            ) {
                userRole?.let {
                    RoleContextPill(role = it)
                }
            }
        }

        // Progress
        AnimatedVisibility(
            visible = showProgress
        ) {
            StepProgressIndicator(
                currentStep = currentStep,
                steps = SignupStep.progressSteps,
                hiddenStepCount = 1
            )
        }
    }
}


@Composable
private fun RoleContextPill(
    modifier: Modifier = Modifier,
    role: UserRole
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.primary.copy(0.25f))
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {

            val value = if (role == UserRole.OWNER) (Icons.Outlined.Storefront to "Owner / Manager")
            else (Icons.Outlined.Person to "Tenant / Mess")

            Icon(
                value.first,
                role.title,
                tint = color.primary,
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = value.second,
                style = typo.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = color.primary,
                    letterSpacing = 0.5.sp
                )
            )

        }

    }

}