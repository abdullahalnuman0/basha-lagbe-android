package dev.abdullah.messseat.features.owner.property_details.components.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoorBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.presentation.components.AppCard
import dev.abdullah.messseat.core.presentation.components.AppDivider
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.widgets.EmptyBox
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun RoomDetailsList(
    searchText: String,
    rooms: List<RoomResponse>,
    onEdit: (Int) -> Unit,
    onAvailability: (Int) -> Unit
) {

    if (rooms.isEmpty()) {
        val isSearching = searchText.isNotBlank()
        EmptyBox(
            title = if (isSearching) "No rooms found" else "No rooms available",
            subtitle = if (isSearching) "No room matches '$searchText'. Try a different room number." else "Add a room to start managing your property.",
            icon = if (isSearching) Icons.Outlined.SearchOff else Icons.Outlined.DoorBack
        )
    } else
        rooms.forEachIndexed { index, room ->
            RoomItemCard(
                room = room,
                onEdit = { onEdit(index) },
                onAvailability = { onAvailability(index) }
            )
        }

}

@Composable
private fun RoomItemCard(
    modifier: Modifier = Modifier,
    room: RoomResponse,
    onEdit: () -> Unit,
    onAvailability: () -> Unit
) {

    val isSeatAvailable = room.availableSeats > 0


    AppCard(modifier) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(Space.screen),
            verticalArrangement = Arrangement.spacedBy(Space.sm)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(Space.sm)
            )
            {

                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = room.images[0].toImageUrl(),
                        contentDescription = room.roomNumber,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(R.drawable.room_default_image),
                        error = painterResource(R.drawable.room_default_image),
                        contentScale = ContentScale.Crop,
                    )

                    Text(
                        "${room.roomWidth} x ${room.roomHeight} ft",
                        style = typos.labelSmall,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(Space.xs)
                            .background(Color.DarkGray.copy(0.5f), RoundedCornerShape(4.dp))
                            .padding(Space.xs)
                    )
                }

                Column(
                    Modifier
                        .weight(1f)
                        .height(90.dp)
                        .padding(vertical = Space.xs),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                room.roomNumber.let {
                                    if (it.startsWith(
                                            "Room",
                                            ignoreCase = true
                                        )
                                    ) it else "Room ${it.trim()}"
                                },
                                style = typos.titleLarge,
                            )

                            AppSpacer(Space.xs)

                            Row(
                                modifier = Modifier.alpha(0.7f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Space.xs)
                            ) {

                                Text(
                                    room.type.name,
                                    style = typos.labelSmall
                                )

                                if (isSeatAvailable) {
                                    Box(
                                        Modifier
                                            .size(4.dp)
                                            .background(Color.Gray, CircleShape)
                                    )

                                    Text(
                                        "${room.availableSeats} seat",
                                        style = typos.labelSmall
                                    )
                                }
                            }

                        }


                        Row(
                            Modifier
                                .background(
                                    if (isSeatAvailable) colors.primaryContainer else colors.secondaryContainer,
                                    CircleShape
                                )
                                .padding(horizontal = Space.sm, vertical = Space.xs),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Space.xs)
                        ) {

                            Box(
                                Modifier
                                    .size(6.dp)
                                    .background(
                                        color = if (isSeatAvailable) colors.primary else colors.error,
                                        CircleShape
                                    )
                            )

                            Text(
                                if (isSeatAvailable) "Available" else "Full",
                                style = typos.labelSmall
                            )
                        }
                    }

                    // --- Rent & deposit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Space.xs),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Space.xs),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                room.rent.toMoney(),
                                style = typos.headlineMedium.copy(color = colors.primary)
                            )

                            Text(
                                "/ mo",
                                style = typos.labelMedium.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = colors.onSurface.copy(0.5f)
                                )
                            )
                        }

                        // dot
                        Box(
                            Modifier
                                .size(4.dp)
                                .background(color = Color.Gray, CircleShape)
                        )

                        Text(
                            "Deposit: ${room.deposit.toMoney()}",
                            style = typos.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface.copy(0.5f)
                            )
                        )

                    }

                }
            }

            AppDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.sm, Alignment.End)
            ) {

                OutlinedButton(onClick = onEdit) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Space.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Edit, "Edit",
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Edit")
                    }
                }

                Button(
                    onClick = onAvailability,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray.copy(0.5f),
                        contentColor = colors.onBackground
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Space.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.EventSeat, "Availability",
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Availability")
                    }
                }


            }
        }

    }

}