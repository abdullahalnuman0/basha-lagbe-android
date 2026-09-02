package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.R
import dev.abdullah.messseat.presentation.auth.AuthInputTextField
import dev.abdullah.messseat.presentation.auth.signup.PasswordRequirements
import dev.abdullah.messseat.presentation.auth.signup.PasswordStrength
import dev.abdullah.messseat.presentation.auth.signup.SignupUiState
import androidx.compose.material3.MaterialTheme.colorScheme as color


@Composable
internal fun SecuritySection(
    modifier: Modifier = Modifier,
    state: SignupUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmPassChange: (String) -> Unit,
    onTermsToggle: (Boolean) -> Unit
) {

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        StepHeader(
            title = "Secure your account",
            subtitle = "Create a strong password to protect yur account and data."
        )

        Spacer(Modifier.height(24.dp))

        AuthInputTextField(
            value = state.password,
            error = state.passwordError,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Create a password",
            isPassword = true,
            icon = painterResource(R.drawable.solar_lock_keyhole),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        AnimatedVisibility(state.password.isNotEmpty()) {
            PasswordStrengthCard(strength = state.strength)
        }

        Spacer(Modifier.height(12.dp))

        PasswordRequirementCard(
            passwordRequirements = state.passwordRequirements
        )

        Spacer(Modifier.height(16.dp))

        AuthInputTextField(
            value = state.confirmPassword,
            error = state.confirmPasswordError,
            onValueChange = onConfirmPassChange,
            label = "Confirm Password",
            placeholder = "Confirm your password",
            isPassword = true,
            icon = painterResource(R.drawable.solar_lock_keyhole),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(Modifier.height(16.dp))

        TermsConditionOption(
            checked = state.isAcceptTermsAndCondition,
            error = state.termsError,
            onCheckedChange = onTermsToggle
        )

    }

}

@Composable
private fun TermsConditionOption(
    modifier: Modifier = Modifier,
    checked: Boolean,
    error: String?,
    onCheckedChange: (Boolean) -> Unit,
    onOpen: (String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)

            PolicyLinkText(
                text = "I accept the Terms & Conditions and Privacy Policy.",
                links = listOf(
                    PolicyLink("Terms & Conditions", "terms"),
                    PolicyLink("Privacy Policy", "privacy"),
                ),
                onOpen = onOpen,
                modifier = modifier,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        InlineErrorMessage(error=error)

    }
}

data class PolicyLink(val text: String, val slug: String)

@Composable
fun PolicyLinkText(
    text: String,
    links: List<PolicyLink>,
    onOpen: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val linkStyle = TextLinkStyles(
        style = SpanStyle(
            color = color.primary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
        ),
    )

    val annotated = buildAnnotatedString {
        var cursor = 0
        // Walk the sentence, emitting plain runs and linked phrases in order.
        while (cursor < text.length) {
            val next = links
                .mapNotNull { link ->
                    val at = text.indexOf(link.text, cursor)
                    if (at >= 0) at to link else null
                }
                .minByOrNull { it.first }

            if (next == null) {
                append(text.substring(cursor))
                break
            }
            val (at, link) = next
            if (at > cursor) append(text.substring(cursor, at))
            withLink(
                LinkAnnotation.Clickable(
                    tag = link.slug,
                    styles = linkStyle,
                    linkInteractionListener = { onOpen(link.slug) },
                ),
            ) {
                append(link.text)
            }
            cursor = at + link.text.length
        }
    }

    Text(annotated, style = style, modifier = modifier)
}


@Composable
 fun PasswordStrengthCard(
    modifier: Modifier = Modifier,
    strength: PasswordStrength
) {

    val barColor by animateColorAsState(
        targetValue = when (strength) {
            PasswordStrength.EMPTY -> color.onBackground
            PasswordStrength.WEAK -> lerp(color.error, color.primary, strength.score)
            PasswordStrength.FAIR -> lerp(color.error, color.primary, strength.score)
            PasswordStrength.MEDIUM -> lerp(color.error, color.primary, strength.score)
            PasswordStrength.STRONG -> lerp(color.error, color.primary, strength.score)
        },
        animationSpec = tween(250),
        label = "strengthColor"
    )

    val barProgress by animateFloatAsState(
        targetValue = strength.score,
        animationSpec = tween(250),
        label = "strengthProgress"
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Strength",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LocalContentColor.current.copy(
                        0.7f
                    )
                )
            )
            Text(
                text = strength.label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color.onBackground.copy(0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(barProgress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }
    }


}

@Composable
 fun PasswordRequirementCard(
    modifier: Modifier = Modifier,
    passwordRequirements: PasswordRequirements
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Password requirements:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current.copy(0.7f)
                )
            )

            RequirementItem(
                label = "At least 8 characters",
                isMet = passwordRequirements.hasMinLength
            )

            RequirementItem(
                label = "One uppercase letter",
                isMet = passwordRequirements.hasUppercase
            )

            RequirementItem(
                label = "One number",
                isMet = passwordRequirements.hasNumber
            )
            RequirementItem(
                label = "One spacial charter",
                isMet = passwordRequirements.hasSpacialChar
            )
        }
    }

}

@Composable
private fun RequirementItem(
    modifier: Modifier = Modifier,
    label: String,
    isMet: Boolean
) {
    val iconColor by animateColorAsState(
        targetValue = if (isMet) color.primary else color.onSurface.copy(0.5f),
        animationSpec = tween(200),
        label = "reqIconColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isMet) color.onSurface else color.onSurface.copy(0.5f),
        animationSpec = tween(200),
        label = "reqTextColor"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (isMet) color.primary.copy(alpha = 0.15f) else Color.Transparent)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isMet) Icons.Filled.Check else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isMet) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
            )
        )

    }

}