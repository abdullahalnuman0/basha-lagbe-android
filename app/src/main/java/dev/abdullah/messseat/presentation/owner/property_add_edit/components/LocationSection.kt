package dev.abdullah.messseat.presentation.owner.property_add_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.presentation.auth.signup.LocationData
import dev.abdullah.messseat.presentation.auth.signup.components.CustomDropdownMenu
import dev.abdullah.messseat.presentation.owner.property_add_edit.PropertyEditorUiState
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSection(
    modifier: Modifier = Modifier,
    state: PropertyEditorUiState,
    onDivisionSelect: (String) -> Unit,
    onDistrictSelect: (String) -> Unit,
    onAreaChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onInstitutionChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var institutionExpand by remember { mutableStateOf(false) }
    var areaExpanded by remember { mutableStateOf(false) }

    val filteredInstitute = remember(state.institutionNearby) {
        if (state.institutionNearby.isBlank())
            LocationData.popularInstitutions.take(5)
        else LocationData.popularInstitutions.filter {
            it.contains(state.institutionNearby, ignoreCase = true)
        }.take(5)
    }


    val suggestedAreas = remember(state.district) {
        if (state.district.isNotEmpty()) {
            LocationData.popularAreasByDistrict[state.district]
                ?: listOf(
                    "Center Area",
                    "Station Road",
                    "College Mor",
                    "Main Road",
                    "Sadar"
                )
        } else {
            emptyList()
        }
    }

    val availableArea = remember(state.area, suggestedAreas) {
        if (state.area.isNotEmpty() && suggestedAreas.isNotEmpty()) {
            suggestedAreas
                .filter {
                    it.contains(state.area, ignoreCase = true)
                }
                .take(5)
        } else {
            emptyList()
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(Modifier.height(Space.md))

        PageHeader(
            title = "Tell us about your place",
            subtitle = "Add the location so tenants can easily find properties near them.",
            titleStyle = typos.headlineMedium,
            subtitleStyle = typos.bodyMedium.copy(
                color = colors.onBackground.copy(0.7f)
            ),
        )

        Spacer(Modifier.height(Space.xxxl))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.lg)
        ) {
            LocationCard(
                modifier = Modifier.weight(1f),
                label = "Division",
                value = state.division,
                items = LocationData.divisions,
                onSelect = onDivisionSelect
            )

            LocationCard(
                modifier = Modifier.weight(1f),
                label = "District",
                value = state.district,
                items = LocationData.districtByDivision[state.division] ?: emptyList(),
                onSelect = onDistrictSelect
            )
        }


        Spacer(Modifier.height(Space.xxl))

        CustomDropdownMenu(
            label = "Area / Neighborhood",
            value = state.area,
            error = state.areaError,
            placeholder = "Search or enter your area",
            icon = rememberVectorPainter(Icons.Outlined.LocationOn),

            isExpanded = areaExpanded && availableArea.isNotEmpty(),

            values = availableArea,

            onValueChange = { newValue ->
                onAreaChange(newValue)

                areaExpanded = newValue.isNotEmpty()
            },

            isEnable = state.district.isNotEmpty(),

            onItemSelected = {
                onAreaChange(it)
                areaExpanded = false
            },

            onExpandedChange = { expanded ->
                areaExpanded = if (availableArea.isNotEmpty()) {
                    expanded
                } else {
                    false
                }
            },

            onDismissRequest = {
                areaExpanded = false
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.height(Space.xxl))

        AuthInputTextField(
            value = state.address,
            error = state.addressError,
            onValueChange = onAddressChange,
            label = "Full Address",
            placeholder = "House/Road, Area, City, District",
            icon = null,
            singleLine = false,
            minLines = 2,
            maxLines = 2,
            shape = 20,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None
            )
        )


        Spacer(Modifier.height(Space.xxl))

        CustomDropdownMenu(
            label = "Nearby Institution",
            subLabel = "University / College / Workplace",
            value = state.institutionNearby,
            placeholder = "Enter your institution name",
            icon = rememberVectorPainter(Icons.Outlined.School),

            isExpanded = institutionExpand && filteredInstitute.isNotEmpty(),

            values = filteredInstitute,

            onValueChange = { newValue ->
                onInstitutionChange(newValue)
                if (!institutionExpand) institutionExpand = true
            },

            onItemSelected = {
                onInstitutionChange(it)
                institutionExpand = false
                focusManager.clearFocus(force = true)
            },

            onExpandedChange = { expanded ->
                institutionExpand = expanded
            },

            onDismissRequest = {
                institutionExpand = false
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    items: List<String>,
    onSelect: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface,
                contentColor = colors.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            onClick = {}
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(Space.lg),
                verticalArrangement = Arrangement.spacedBy(Space.sm)
            ) {
                Text(
                    label,
                    style = typos.labelMedium.copy(
                        color = colors.onSurface.copy(0.7f)
                    )
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        value,
                        style = typos.bodyLarge.copy(
                            color = colors.onSurface.copy(0.9f)
                        )
                    )
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }

        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.surface)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = typos.bodyMedium.copy(
                                fontWeight = if (value == item) FontWeight.Bold else FontWeight.Normal,
                                color = if (value == item) colors.primary else colors.onSurface
                            )
                        )
                    },
                    onClick = {
                        expanded = false
                        onSelect(item)
                    }
                )
            }
        }
    }

}