package dev.abdullah.messseat.features.owner.property_add_edit.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Balcony
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Roofing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.core.presentation.components.ErrorBoxMessage
import dev.abdullah.messseat.core.presentation.components.LocalSnackbar
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.features.auth.signup.components.dashedBorder
import dev.abdullah.messseat.features.owner.property_add_edit.PropertyEditorUiState
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

data class Facility(
    val label: String,
    val icon: ImageVector
)

val facilityList = listOf(
    Facility("Wi-Fi", Icons.Default.Wifi),
    Facility("Parking", Icons.Default.LocalParking),
    Facility("Electricity", Icons.Default.Bolt),
    Facility("Water Supply", Icons.Default.WaterDrop),
    Facility("Gas", Icons.Default.LocalFireDepartment),
    Facility("Air Conditioning", Icons.Default.AcUnit),
    Facility("Heating", Icons.Default.Thermostat),
    Facility("Furnished", Icons.Default.Chair),
    Facility("Kitchen", Icons.Default.Kitchen),
    Facility("Attached Bathroom", Icons.Default.Bathtub),
    Facility("Balcony", Icons.Default.Balcony),
    Facility("Lift / Elevator", Icons.Default.Elevator),
    Facility("Security", Icons.Default.Security),
    Facility("CCTV", Icons.Default.Videocam),
    Facility("Generator", Icons.Default.Power),
    Facility("Laundry", Icons.Default.LocalLaundryService),
    Facility("Dining Area", Icons.Default.TableRestaurant),
    Facility("Common Room", Icons.Default.Groups),
    Facility("Rooftop", Icons.Default.Roofing),
    Facility("Garden", Icons.Default.Yard),
    Facility("Gym", Icons.Default.FitnessCenter),
    Facility("Swimming Pool", Icons.Default.Pool),
    Facility("Study Room", Icons.AutoMirrored.Default.MenuBook),
    Facility("Storage", Icons.Default.Storage),
    Facility("24/7 Water Supply", Icons.Default.WaterDrop)
)
private val facilityIconMap = facilityList.associateBy {
    it.label.trim().lowercase()
}

fun String.toFacilityIcon(): ImageVector {
    return facilityIconMap[this.lowercase()]?.icon
        ?: Icons.AutoMirrored.Outlined.HelpOutline
}


@Composable
fun FacilitiesSection(
    modifier: Modifier = Modifier,
    state: PropertyEditorUiState,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
    onAllRemove: () -> Unit
) {

    val snackbar = LocalSnackbar.current

    val isNoEmpty = state.facilities.isNotEmpty()
    val facilities = remember { mutableStateListOf<Facility>().apply { addAll(facilityList) } }
    var showAddFacilityDialog by remember { mutableStateOf(false) }


    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Spacer(Modifier.height(Space.md))

        PageHeader(
            title = "Available facilities",
            subtitle = "Select everything your place provides for tenants.",
            titleStyle = typos.headlineMedium,
            subtitleStyle = typos.bodyMedium.copy(
                color = colors.onBackground.copy(0.7f)
            ),
        )

        Spacer(Modifier.height(Space.xxxl))

        ErrorBoxMessage(
            error = state.facilitiesError,
            modifier = Modifier.padding(bottom = Space.xl)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.md)
        )
        {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = isNoEmpty, onClick = onAllRemove),
                color = colors.tertiary,
                contentColor = colors.onTertiary,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = if (isNoEmpty) Icons.Filled.Check else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "institution isn't listed",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = " ${if (isNoEmpty) state.facilities.size else "No"} facilities selected",
                        style = typos.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
            }

            AnimatedVisibility(isNoEmpty) {
                Text(
                    "Tap to toggle",
                    style = typos.labelSmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }

        }

        Spacer(Modifier.height(Space.xl))

        FacilitiesOptions(
            facilities = facilities.sortedByDescending { it.label in state.facilities },
            selectedFacilities = state.facilities,
            onAdd = onAdd,
            onRemove = onRemove
        )


        Spacer(Modifier.height(Space.xl))

        OutlinedButton(
            onClick = { showAddFacilityDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = colors.outline,
                    strokeWidth = 1.dp,
                    cornerRadius = 16.dp
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                Modifier.height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.md, Alignment.CenterHorizontally)
            ) {
                Icon(
                    Icons.Default.Add, "add",
                    tint = colors.primary
                )

                Text(
                    "Add custom facility",
                    color = colors.primary
                )
            }
        }

        Spacer(Modifier.height(Space.xxxl))


        if (showAddFacilityDialog) {

            AddCustomFacilityDialog(
                existingFacilities = facilities,
                onDismiss = {
                    showAddFacilityDialog = false
                },
                onAdd = { name ->

                    if (state.facilities.contains(name))
                        snackbar.show("This facility already exists.")
                    else {
                        facilities.add(
                            Facility(
                                label = name,
                                icon = Icons.Default.AddCircle
                            )
                        )
                        onAdd(name)
                    }

                    showAddFacilityDialog = false
                }
            )
        }
    }

}

@Composable
private fun AddCustomFacilityDialog(
    existingFacilities: List<Facility>,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {

    var facilityName by remember {
        mutableStateOf("")
    }

    val trimmedName = facilityName.trim()

    val alreadyExists = existingFacilities.any {
        it.label.equals(trimmedName, ignoreCase = true)
    }

    val isValid = trimmedName.isNotEmpty() && !alreadyExists

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        title = {
            Text(
                text = "Add custom facility"
            )
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Add a facility that isn't listed above.",
                    style = typos.bodyMedium
                )

                AuthInputTextField(
                    value = facilityName,
                    error = if (alreadyExists) "This facility already exists." else null,
                    onValueChange = {
                        facilityName = it
                    },
                    placeholder = "e.g. Rooftop",
                    icon = rememberVectorPainter(Icons.Outlined.Category)
                )

            }
        },

        confirmButton = {
            Button(
                onClick = {
                    if (isValid) {
                        onAdd(trimmedName)
                    }
                },
                enabled = isValid
            ) {
                Text("Add")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
private fun FacilitiesOptions(
    modifier: Modifier = Modifier,
    facilities: List<Facility>,
    selectedFacilities: Set<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit
) {

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        facilities.forEach { item ->
            val isSelected = selectedFacilities.contains(item.label)

            FacilityChip(
                label = item.label,
                icon = item.icon,
                isSelected = isSelected,
                onClick = { if (isSelected) onRemove(item.label) else onAdd(item.label) }
            )
        }
    }

}

@Composable
fun FacilityChip(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {

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
        modifier = modifier
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
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.let { onClick() } }
            ),
        color = backgroundColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            Modifier.padding(horizontal = Space.md, vertical = Space.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, label,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = typos.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }

}