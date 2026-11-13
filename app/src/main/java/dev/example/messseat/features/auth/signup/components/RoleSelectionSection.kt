package dev.example.messseat.features.auth.signup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.example.messseat.R
import dev.example.messseat.core.presentation.widgets.PageHeader
import dev.example.messseat.domain.enums.UserRole
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.ui.theme.AppThemeExt
import androidx.compose.material3.MaterialTheme.colorScheme as colors

@Composable
internal fun RoleSelectionSection(
    modifier: Modifier = Modifier,
    userRole: UserRole?,
    onRoleSelected: (UserRole) -> Unit,
) {

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(4.dp))

        PageHeader(
            title = "What are you looking tor?",
            subtitle = "Choose how you want to user ${stringResource(R.string.app_name)} to get the best experience."
        )

        Spacer(Modifier.height(24.dp))

        UserRole.entries.forEach { role ->
            RoleSelectableCard(
                role = role,
                isSelected = userRole == role,
                onClick = { onRoleSelected(role) }
            )
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(16.dp))

    }

}

@Composable
private fun RoleSelectableCard(
    modifier: Modifier = Modifier,
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else AppThemeExt.colors.cardBorder,
        animationSpec = tween(200),
        label = "borderColor"
    )

    val cardElevation by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 1.dp,
        animationSpec = tween(200),
        label = "cardElevation"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(200),
        label = "textAlpha"
    )

    SectionCard(
        contentPadding = PaddingValues(0.dp),
        borderStroke = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        onClick = onClick
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) colors.primaryContainer else colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        role.icon,
                        role.title,
                        tint = if (isSelected) colors.primary else colors.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }


                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }

            }


            Spacer(Modifier.height(16.dp))

            // --- Uppercase Category Label ---
            Text(
                text = role.subTitle.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 11.sp,
                    color = if (isSelected) colors.primary else colors.onSurface
                ),
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(Modifier.height(4.dp))

            // --- title ---
            Text(
                role.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = role.desc,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                ),
                modifier = Modifier.alpha(textAlpha)
            )


        }

    }


}