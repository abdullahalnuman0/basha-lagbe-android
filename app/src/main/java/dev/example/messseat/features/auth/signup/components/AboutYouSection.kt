package dev.example.messseat.features.auth.signup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.widgets.PageHeader
import dev.example.messseat.domain.enums.Gender
import dev.example.messseat.core.presentation.components.AuthInputTextField
import dev.example.messseat.core.presentation.components.InlineMessage
import dev.example.messseat.features.auth.signup.LocationData
import dev.example.messseat.features.auth.signup.SignupUiState
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.ui.theme.AppThemeExt
import androidx.compose.material3.MaterialTheme.colorScheme as color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutYouSection(
    modifier: Modifier = Modifier,
    state: SignupUiState,
    onGenderSelected: (Gender) -> Unit,
    onInstitutionChange: (String) -> Unit,
    onInstitutionNotListedToggle: (Boolean) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    var isExpanded by remember { mutableStateOf(false) }

    val filteredInstitute = remember(state.institution) {
        if (state.institution.isBlank())
            LocationData.popularInstitutions.take(5)
        else LocationData.popularInstitutions.filter {
            it.contains(state.institution, ignoreCase = true)
        }.take(5)
    }


    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        PageHeader(
            title = "Tell us about you",
            subtitle = "This helps us provide a more relevant experience."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Gender ---

        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Gender.entries.forEach { gender ->
                GenderSelectableCard(
                    modifier = Modifier.weight(1f),
                    gender = gender,
                    isSelected = state.gender == gender,
                    onClick = { onGenderSelected(gender) }
                )
            }
        }
        InlineMessage(message = state.genderError, isError = true)

        Spacer(Modifier.height(height = 24.dp))

        // --- Institute ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Institute",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "University / College / Workplace",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.alpha(0.5f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded && filteredInstitute.isNotEmpty() && !state.isCustomInstitution,
            onExpandedChange = { isExpanded = it }
        )
        {

            AuthInputTextField(
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                value = state.institution,
                error = state.institutionError,
                onValueChange = {
                    onInstitutionChange(it)
                    if (!isExpanded) isExpanded = true
                },
                placeholder = if (state.isCustomInstitution) "Enter your institution name" else "Search your institution...",
                icon = rememberVectorPainter(if (state.isCustomInstitution) Icons.Outlined.Apartment else Icons.Outlined.School),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(color.surface),
                expanded = isExpanded && filteredInstitute.isNotEmpty() && !state.isCustomInstitution,
                onDismissRequest = { isExpanded = false }
            ) {
                filteredInstitute.forEach { inst ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                inst,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = color.onSurface
                                )
                            )
                        },
                        onClick = {
                            onInstitutionChange(inst)
                            isExpanded = false
                            focusManager.clearFocus(force = true)
                        }
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onInstitutionNotListedToggle(!state.isCustomInstitution) },
            color = if (state.isCustomInstitution) color.primary.copy(0.05f) else color.tertiary.copy(
                0.05f
            ),
            contentColor = if (state.isCustomInstitution) color.primary else color.tertiary,
            shape = RoundedCornerShape(8.dp)
        ) {

            Row(
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = if (state.isCustomInstitution) Icons.Filled.Check else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "institution isn't listed",
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (state.isCustomInstitution) "Institution name" else "My institution isn't listed",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Quick select chips for
        Text(
            text = "Popular Suggestions:",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.alpha(0.5f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val quickSuggestions =
            listOf("DU", "BUET", "NSU", "BRAC University", "AIUB", "Job / Corporate")

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            quickSuggestions.forEach { tag ->
                val isSelected = state.institution.contains(tag, ignoreCase = true)
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) color.primary else color.onBackground.copy(0.2f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            val fullTag = when (tag) {
                                "DU" -> "University of Dhaka (DU)"
                                "BUET" -> "Bangladesh University of Engineering and Technology (BUET)"
                                "NSU" -> "North South University (NSU)"
                                "BRAC University" -> "BRAC University"
                                "AIUB" -> "American International University-Bangladesh (AIUB)"
                                "Job / Corporate" -> "Working Professional / Corporate"
                                else -> tag
                            }
                            onInstitutionChange(fullTag)
                            onInstitutionNotListedToggle(false)
                            isExpanded = false
                        },
                    color = if (isSelected) color.primaryContainer else color.surface,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) color.primary else color.onBackground
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

    }

}

@Composable
private fun GenderSelectableCard(
    modifier: Modifier = Modifier,
    gender: Gender,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) color.primary else AppThemeExt.colors.cardBorder,
        animationSpec = tween(200),
        label = "borderColor"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        animationSpec = tween(200),
        label = "borderWidth"
    )

    SectionCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        borderStroke = BorderStroke(
            width = borderWidth,
            color = borderColor
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) color.primary else color.onSurface.copy(0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = gender.icon,
                    contentDescription = gender.label,
                    tint = color.onSurface,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (isSelected) 1f else 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gender.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) color.primary else color.onSurface
                )
            )
        }
    }

}