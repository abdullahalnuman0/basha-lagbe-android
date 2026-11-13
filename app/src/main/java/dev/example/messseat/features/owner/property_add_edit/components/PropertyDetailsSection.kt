package dev.example.messseat.features.owner.property_add_edit.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.ErrorBoxMessage
import dev.example.messseat.core.presentation.widgets.PageHeader
import dev.example.messseat.domain.enums.PropertyGender
import dev.example.messseat.domain.enums.PropertyType
import dev.example.messseat.features.owner.property_add_edit.PropertyEditorUiState
import dev.example.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyDetailsSection(
    modifier: Modifier = Modifier,
    state: PropertyEditorUiState,
    onTypeSelect: (PropertyType) -> Unit,
    onGenderSelect: (PropertyGender) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(Modifier.height(Space.md))

        PageHeader(
            title = "Property details",
            subtitle = "Provide some details about the type and preferred gender.",
            titleStyle = typos.headlineMedium,
            subtitleStyle = typos.bodyMedium.copy(
                color = colors.onBackground.copy(0.7f)
            ),
        )

        Spacer(Modifier.height(Space.xxxl))

        state.propertyDetailsError?.let {
            ErrorBoxMessage(error = it)
            Spacer(Modifier.height(Space.xxxl))
        }

        PartLabel(label = "Property Type", info = "Required")
        Spacer(Modifier.height(Space.md))

        Column(
            verticalArrangement = Arrangement.spacedBy(Space.listGap)
        ) {
            PropertyType.entries.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.listGap)
                ) {
                    rowItems.forEach { item ->
                        SelectedCard(
                            Modifier.weight(1f),
                            item.title,
                            item.icon,
                            isSelected = state.type == item,
                            onClick = { onTypeSelect(item) }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(Space.xxl))

        PartLabel(label = "Suitable For", info = "Target Occupants")

        Spacer(Modifier.height(Space.md))


        Column(
            verticalArrangement = Arrangement.spacedBy(Space.listGap)
        ) {
            PropertyGender.entries.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.listGap)
                ) {
                    rowItems.forEach { item ->
                        SelectedCard(
                            Modifier.weight(1f),
                            item.title,
                            item.icon,
                            shape = CircleShape,
                            isSelected = state.gender == item,
                            onClick = { onGenderSelect(item) }
                        )
                    }
                }
            }
        }


    }
}



@Composable
private fun PartLabel(
    modifier: Modifier = Modifier,
    label: String,
    info: String
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            label,
            style = typos.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            info,
            style = typos.bodySmall.copy(
                color = LocalContentColor.current.copy(0.7f),
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun SelectedCard(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(
            if (isSelected) 1.dp else 0.dp,
            if (isSelected) colors.primary else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = Space.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                Modifier
                    .size(40.dp)
                    .background(
                        if (isSelected) colors.primary else colors.secondary,
                        shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, label,
                    tint = if (isSelected) colors.onPrimary else colors.onSecondary
                )
            }

            Spacer(Modifier.height(Space.sm))

            Text(
                label,
                style = typos.labelLarge.copy(
                    color = if (isSelected) colors.primary else colors.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }

}