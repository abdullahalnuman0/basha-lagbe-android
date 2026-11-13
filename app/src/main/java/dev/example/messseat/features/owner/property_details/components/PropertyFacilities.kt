package dev.example.messseat.features.owner.property_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.AppSpacer
import dev.example.messseat.features.owner.property_add_edit.components.FacilityChip
import dev.example.messseat.features.owner.property_add_edit.components.toFacilityIcon
import dev.example.messseat.ui.theme.Space

import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyFacilities(
    modifier: Modifier = Modifier,
    facilities: List<String>,
    facilityLimit: Int = 8,
    shape: Shape = RectangleShape,
    ) {
    var isExpanded by remember { mutableStateOf(false) }
    val isTruncated = facilities.size > facilityLimit

    Surface(
        shadowElevation = 1.dp,
        shape = shape
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .background(colors.surface)
                .padding(Space.screen),
        ) {

            Text(
                "Facilities & Amenities",
                style = typos.titleLarge
            )
            AppSpacer(Space.sm)

            Column(
                verticalArrangement = Arrangement.spacedBy(Space.listGap)
            ) {
                facilities.take(if (isExpanded) facilities.size else facilityLimit).chunked(2)
                    .forEach { rowItem ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Space.listGap)
                        ) {
                            rowItem.forEach { facility ->
                                FacilityChip(
                                    modifier = Modifier.weight(1f),
                                    label = facility,
                                    icon = facility.toFacilityIcon()
                                )
                            }

                            if (rowItem.size % 2 != 0)
                                AppSpacer(Space.xs, Modifier.weight(1f))
                        }
                    }
            }


            if (isTruncated) {
                AppSpacer(Space.sm)
                Text(
                    if (isExpanded) "Show less"
                    else "+${facilities.size - facilityLimit} more amenities",
                    color = colors.primary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                )
            }

        }
    }
}