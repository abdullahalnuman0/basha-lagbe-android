package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import dev.abdullah.messseat.R
import dev.abdullah.messseat.domain.model.UserRole
import androidx.compose.material3.MaterialTheme.colorScheme as color

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

        StepHeader(
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
        targetValue = if (isSelected) color.primary else Color.Unspecified,
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(32.dp)
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = color.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
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
                        .background(if (isSelected) color.primaryContainer else color.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        role.icon,
                        role.title,
                        tint = if (isSelected) color.primary else color.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }


                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color.primary),
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
                    color = if (isSelected) color.primary else color.onSurface
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