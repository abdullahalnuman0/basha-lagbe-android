package dev.abdullah.messseat.presentation.owner.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.presentation.components.AppCard
import dev.abdullah.messseat.core.presentation.components.AppDivider
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.data.enums.PropertyType
import dev.abdullah.messseat.ui.theme.Space
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos


@Composable
fun ListingItem(
    modifier: Modifier = Modifier,
    property: PropertyResponse,
    moreContent: (@Composable () -> Unit)? = null,
    rooms: List<RoomResponse> = property.rooms
) {
    if (rooms.isEmpty()) return

    var currentRoomIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    LaunchedEffect(rooms) {
        if (rooms.size <= 1) return@LaunchedEffect

        while (true) {
            delay(5.seconds)

            currentRoomIndex =
                (currentRoomIndex + 1) % rooms.size
        }
    }

    val room = rooms[currentRoomIndex]

    AppCard(modifier) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {

                // Image + rent smooth transition
                AnimatedContent(
                    targetState = room,
                    transitionSpec = {
                        (
                                fadeIn(
                                    animationSpec = tween(700)
                                ) + slideInHorizontally(
                                    animationSpec = tween(700),
                                    initialOffsetX = { it / 4 }
                                )
                                ).togetherWith(
                                fadeOut(
                                    animationSpec = tween(500)
                                )
                            )
                    },
                    label = "room_transition"
                ) { targetRoom ->

                    AsyncImage(
                        model = targetRoom.images
                            .firstOrNull()
                            ?.toImageUrl(),
                        contentDescription = targetRoom.roomNumber,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = rememberAsyncImagePainter(
                            R.drawable.room_default_image
                        ),
                        error = rememberAsyncImagePainter(
                            R.drawable.room_default_image
                        )
                    )
                }

                Box(
                    Modifier
                        .matchParentSize()
                        .padding(Space.md)
                ) {

                    Row(
                        Modifier.align(Alignment.TopStart),
                        horizontalArrangement = Arrangement.spacedBy(Space.xs)
                    ) {

                        ListingItemClip(
                            label = property.type.name,
                            icon = Icons.Default.Hotel
                        )

                        ListingItemClip(
                            label = property.gender.name,
                            bg = colors.tertiary,
                            onBg = colors.onTertiary
                        )
                    }

                    // Price also changes smoothly
                    AnimatedContent(
                        modifier = Modifier.align(Alignment.TopEnd),
                        targetState = room.rent,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(500)
                            ) togetherWith fadeOut(
                                animationSpec = tween(300)
                            )
                        },
                        label = "rent_transition"
                    ) { rent ->

                        ListingItemClip(
                            modifier = Modifier.align(Alignment.TopEnd),
                            label = "${rent.toMoney()}/${
                                if (property.type in listOf(
                                        PropertyType.MESS,
                                        PropertyType.HOSTEL
                                    )
                                ) "seat" else "month"
                            }",
                            bg = Color.White,
                            onBg = colors.primary
                        )
                    }

                    Row(
                        Modifier.align(Alignment.BottomStart),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Space.xs)
                    ) {

                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "location",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )

                        Text(
                            text = "${property.area}, ${property.district}",
                            style = typos.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Space.lg,
                        vertical = Space.md
                    )
            ) {

                if (!property.approved) {
                    PendingAlert()
                    Spacer(Modifier.height(Space.sm))
                }

                Text(
                    property.title,
                    style = typos.headlineSmall
                )

                Spacer(Modifier.height(Space.xs))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Space.xs)
                ) {

                    Icon(
                        Icons.Outlined.School,
                        contentDescription = "institution",
                        modifier = Modifier.size(16.dp),
                        tint = colors.primary
                    )

                    Text(
                        text = property.institutionNearby,
                        style = typos.labelMedium.copy(
                            color = LocalContentColor.current.copy(0.5f)
                        )
                    )
                }

                Spacer(Modifier.height(Space.sm))

                AppDivider()

                Spacer(Modifier.height(Space.sm))

                AvailableValueCard(
                    rooms = rooms.size,
                    seats = rooms.sumOf { it.availableSeats }
                )

                moreContent?.let {
                    Spacer(Modifier.height(Space.sm))
                    moreContent()
                }
            }


        }
    }
}


@Composable
fun ListingItemBtns(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onView: () -> Unit
) {

    Row(
        modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(Space.md)
    ) {

        OutlinedButton(onClick = onEdit) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.sm)
            ) {
                Icon(Icons.Outlined.Edit, "Edit")
                Text("Edit Listing")
            }
        }

        Button(onClick = onView, Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.sm)
            ) {
                Icon(Icons.Outlined.RemoveRedEye, "View Details")
                Text("View Details")
            }
        }
    }

}

@Composable
fun AvailableValueCard(
    modifier: Modifier = Modifier,
    rooms: Int,
    seats: Int
) {
    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.xs)
        ) {
            Icon(
                Icons.Default.MeetingRoom,
                "Rooms",
                Modifier.size(16.dp),
            )
            Text(
                text = "$rooms Rooms",
                style = typos.labelMedium
            )
        }

        VerticalDivider(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.xs)
        ) {
            Icon(
                Icons.Default.Hotel,
                "Seats",
                Modifier.size(16.dp),
            )
            Text(
                text = "$seats Available Seats",
                style = typos.labelMedium
            )
        }


    }

}

@Composable
fun PendingAlert(modifier: Modifier = Modifier) {

    Row(
        modifier
            .fillMaxWidth()
            .background(colors.errorContainer.copy(0.1f), RoundedCornerShape(8.dp))
            .border(
                width = 0.1.dp,
                color = colors.error,
                RoundedCornerShape(8.dp)
            )
            .padding(Space.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.xs)
        ) {
            Icon(
                Icons.Default.AccessTime,
                "Pending",
                Modifier.size(16.dp),
                tint = colors.error
            )
            Text(
                text = "Pending Approval",
                style = typos.labelMedium.copy(
                    color = colors.error
                )
            )
        }

        VerticalDivider(Modifier.height(12.dp))

        Text(
            text = "Waiting for the approval.",
            style = typos.labelMedium.copy(
                color = LocalContentColor.current.copy(0.5f)
            )
        )

    }

}

@Composable
fun ListingItemClip(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector? = null,
    bg: Color = colors.background,
    onBg: Color = colors.onBackground
) {

    Row(
        modifier
            .background(bg, CircleShape)
            .clip(CircleShape)
            .padding(horizontal = Space.sm, vertical = Space.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Space.xs)
    ) {

        icon?.let {
            Icon(
                icon, label,
                Modifier.size(16.dp),
                tint = onBg
            )
        }

        Text(
            text = label.uppercase(),
            style = typos.labelLarge.copy(
                color = onBg
            )
        )

    }

}