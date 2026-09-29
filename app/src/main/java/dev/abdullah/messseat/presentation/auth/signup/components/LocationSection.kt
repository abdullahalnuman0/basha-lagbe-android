package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.presentation.auth.signup.LocationData
import dev.abdullah.messseat.presentation.auth.signup.SignupUiState
import androidx.compose.material3.MaterialTheme.colorScheme as colors


@Composable
internal fun LocationSection(
    modifier: Modifier = Modifier,
    state: SignupUiState,
    onDivisionSelected: (String) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onAreaSelected: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var areaExpanded by remember { mutableStateOf(false) }

    // --- Available districts by division
    val availableDistricts = remember(state.division) {
        if (state.division.isNotEmpty())
            LocationData.districtByDivision[state.division] ?: emptyList()
        else emptyList()
    }

    val suggestedAreas = remember(state.district) {
        if (state.district.isNotEmpty())
            LocationData.popularAreasByDistrict[state.district] ?: listOf(
                "Center Area",
                "Station Road",
                "College Mor",
                "Main Road",
                "Sadar"
            )
        else emptyList()
    }

    // -- available are by district
    val availableArea = remember(state.area, suggestedAreas) {
        if (state.area.isNotEmpty() && suggestedAreas.isNotEmpty()) suggestedAreas.take(5)
        else emptyList()
    }


    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        PageHeader(
            title = "Where are you located?",
            subtitle = "Choose your location to discover relevant homes and messes."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- division dropdown ---
        CustomDropdownMenu(
            label = "Division",
            value = state.division,
            error = state.divisionError,
            placeholder = "Select division",
            icon = rememberVectorPainter(Icons.Outlined.Map),
            isExpanded = divisionExpanded,
            values = LocationData.divisions,
            readOnly = true,
            onItemSelected = {
                onDivisionSelected(it)
                divisionExpanded = false
            },
            onExpandedChange = { divisionExpanded = it },
            onDismissRequest = { divisionExpanded = false }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- district dropdown ---
        CustomDropdownMenu(
            label = "District",
            subLabel = "Select division first",
            value = state.district,
            error = state.districtError,
            placeholder = if (state.division.isBlank()) "Select division first" else "Select district",
            icon = rememberVectorPainter(Icons.Outlined.LocationCity),
            isExpanded = districtExpanded,
            values = availableDistricts,
            readOnly = true,
            isEnable = state.division.isNotEmpty(),
            onItemSelected = {
                onDistrictSelected(it)
                districtExpanded = false
            },
            onExpandedChange = { if (state.division.isNotEmpty()) districtExpanded = it },
            onDismissRequest = { districtExpanded = false }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- area ---
        CustomDropdownMenu(
            label = "Area / Thana / Neighborhood",
            value = state.area,
            error = state.areaError,
            placeholder = "Search or enter your area",
            icon = rememberVectorPainter(Icons.Outlined.LocationOn),
            isExpanded = areaExpanded && availableArea.isNotEmpty(),
            values = availableArea,
            onValueChange = {
                onAreaSelected(it)
                areaExpanded = true
            },
            isEnable = state.district.isNotEmpty(),
            onItemSelected = {
                onAreaSelected(it)
                areaExpanded = false
                focusManager.clearFocus()
            },
            onExpandedChange = { if (state.district.isNotEmpty()) areaExpanded = it },
            onDismissRequest = { areaExpanded = false },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // -- popular are chips for quick select
        if (state.district.isNotEmpty()) {

            Text(
                text = "Popular areas in ${state.district}:",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LocalContentColor.current.copy(0.5f),
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                suggestedAreas.forEach { areaName ->

                    val isSelected = state.area.equals(areaName, ignoreCase = true)

                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.04f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "areaScale"
                    )

                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) colors.primaryContainer else colors.surface,
                        animationSpec = tween(250),
                        label = "areaBackground"
                    )

                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) colors.primary else colors.onBackground.copy(
                            alpha = 0.2f
                        ),
                        animationSpec = tween(250),
                        label = "areaBorder"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) colors.primary else colors.onBackground,
                        animationSpec = tween(250),
                        label = "areaText"
                    )

                    Surface(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onAreaSelected(areaName)
                                areaExpanded = false
                            },
                        color = backgroundColor,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = areaName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = textColor
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            /*FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                suggestedAreas.forEach { areaName ->
                    val isSelected = state.area.equals(areaName, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = 1.dp,
                                colors = if (isSelected) colors.primary else colors.onBackground.copy(
                                    0.2f
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                onAreaSelected(areaName)
                                areaExpanded = false
                            },
                        colors = if (isSelected) colors.primaryContainer else colors.surface,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = areaName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                colors = if (isSelected) colors.primary else colors.onBackground
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }*/
        }


    }

}

