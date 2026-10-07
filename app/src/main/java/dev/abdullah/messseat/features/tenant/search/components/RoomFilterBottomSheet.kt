package dev.abdullah.messseat.features.tenant.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.components.AuthInputTextField
import dev.abdullah.messseat.data.enums.RoomType
import dev.abdullah.messseat.features.auth.signup.LocationData
import dev.abdullah.messseat.features.owner.property_add_edit.components.LocationCard
import dev.abdullah.messseat.features.tenant.search.RoomSearchFilter
import dev.abdullah.messseat.features.tenant.search.SortOption
import dev.abdullah.messseat.features.tenant.search.SortOrder
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomFilterBottomSheet(
    filter: RoomSearchFilter,
    onFilterChange: ((RoomSearchFilter) -> RoomSearchFilter) -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        contentColor = colors.onSurface,
        tonalElevation = 4.dp,
        sheetGesturesEnabled = false,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        )
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(
                    start = Space.screen,
                    end = Space.screen,
//                    bottom = Space.xl
                )
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
            {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filters",
                        style = typos.headlineMedium
                    )

                    TextButton(
                        onClick = onClear,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = colors.error
                        )
                    ) {
                        Text("Clear all")
                    }
                }

                Text("Location")


                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.lg)
                ) {
                    LocationCard(
                        modifier = Modifier.weight(1f),
                        label = "Division",
                        value = filter.division,
                        items = LocationData.divisions,
                        onSelect = {
                            onFilterChange { old ->
                                old.copy(division = it)
                            }
                        }
                    )

                    LocationCard(
                        modifier = Modifier.weight(1f),
                        label = "District",
                        value = filter.district,
                        items = LocationData.districtByDivision[filter.division] ?: emptyList(),
                        onSelect = {
                            onFilterChange { old ->
                                old.copy(district = it)
                            }
                        }
                    )
                }

                AuthInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = filter.area,
                    onValueChange = {
                        onFilterChange { old ->
                            old.copy(area = it)
                        }
                    },
                    placeholder = "Area / Neighborhood",
                    icon = null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                )

                Text("Room type")

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        RoomType.entries
                    ) { type ->

                        FilterChip(
                            selected = filter.roomType == type,
                            onClick = {
                                onFilterChange { old ->
                                    old.copy(
                                        roomType =
                                            if (old.roomType == type) {
                                                null
                                            } else {
                                                type
                                            }
                                    )
                                }
                            },
                            label = {
                                Text(
                                    type.name
                                        .lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                )
                            }
                        )
                    }
                }

                Text("Rent")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.md)
                ) {

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.minRent,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(minRent = it.filter(Char::isDigit))
                            }
                        },
//                    label = "Min",
                        placeholder = "Min: e.g. 1000",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.maxRent,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(maxRent = it.filter(Char::isDigit))
                            }
                        },
                        placeholder = "Max: e.g. 1200",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                }

                Text("Available seats")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.md)
                ) {

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.minAvailableSeats,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(
                                    minAvailableSeats =
                                        it.filter(Char::isDigit)
                                )
                            }
                        },
                        placeholder = "Min seats",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.maxAvailableSeats,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(
                                    maxAvailableSeats =
                                        it.filter(Char::isDigit)
                                )
                            }
                        },
                        placeholder = "Max seats",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                }


                Text("Minimum room size (ft)")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.md)
                ) {

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.minRoomWidth,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(
                                    minRoomWidth =
                                        it.filter(Char::isDigit)
                                )
                            }
                        },
                        placeholder = "Min width",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )

                    AuthInputTextField(
                        modifier = Modifier.weight(1f),
                        value = filter.minRoomHeight,
                        onValueChange = {
                            onFilterChange { old ->
                                old.copy(
                                    minRoomHeight =
                                        it.filter(Char::isDigit)
                                )
                            }
                        },
                        placeholder = "Min height",
                        icon = null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                }


                Text("Sort by")

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SortOption.entries) { option ->

                        FilterChip(
                            selected = filter.sortBy == option,
                            onClick = {
                                onFilterChange { old ->
                                    old.copy(
                                        sortBy = option
                                    )
                                }
                            },
                            label = {
                                Text(option.title)
                            }
                        )
                    }
                }

                Text("Order")

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SortOrder.entries) { order ->

                        FilterChip(
                            selected = filter.sortOrder == order,
                            onClick = {
                                onFilterChange { old ->
                                    old.copy(
                                        sortOrder = order
                                    )
                                }
                            },
                            label = {
                                Text(order.title)
                            }
                        )
                    }
                }

                AppSpacer(Space.screen)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = Space.md),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .height(52.dp)
                ) {
                    Text("Close")
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text("Apply filters")
                }


            }
        }
    }
}


