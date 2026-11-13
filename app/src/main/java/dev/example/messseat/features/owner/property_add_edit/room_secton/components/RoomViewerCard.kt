package dev.example.messseat.features.owner.property_add_edit.room_secton.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.example.messseat.BuildConfig
import dev.example.messseat.core.presentation.components.AppDivider
import dev.example.messseat.core.presentation.components.DeleteConfirmationDialog
import dev.example.messseat.domain.model.Room
import dev.example.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun RoomViewerCard(
    modifier: Modifier = Modifier,
    room: Room,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val seatDotColor by rememberInfiniteTransition(label = "seat_indicator").animateColor(
        initialValue = colors.primary,
        targetValue = lerp(colors.primary, colors.onSurface, 0.5f),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_color"
    )
    Card(
        modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = Space.lg,
                    end = Space.lg,
                    top = Space.xxl,
                    bottom = Space.md
                ),
            verticalArrangement = Arrangement.spacedBy(Space.md)
        ) {

            // --- Room number & type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.MeetingRoom,
                        "RoomNumber",
                        tint = colors.primary
                    )
                    Text(
                        room.roomNumber,
                        style = typos.headlineMedium.copy(fontWeight = FontWeight.Normal)
                    )
                }

                Text(
                    room.type?.name ?: "",
                    style = typos.labelSmall.copy(color = colors.onSurface.copy(0.5f)),
                    modifier = Modifier
                        .background(colors.secondary.copy(0.5f), CircleShape)
                        .padding(horizontal = Space.sm, vertical = Space.xs)
                )

            }

            // --- Rent & deposit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.lg),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "৳ ${room.rent}",
                        style = typos.headlineMedium.copy(color = colors.primary)
                    )

                    Text(
                        "/ month",
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
                    "Deposit: ৳ ${room.deposit}",
                    style = typos.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface.copy(0.5f)
                    )
                )

            }

            // --- Available seats & room size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.lg),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .background(color = seatDotColor, CircleShape)
                    )

                    Text(
                        "${room.availableSeats} seats available",
                        style = typos.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                }

                // dot
                Box(
                    Modifier
                        .size(4.dp)
                        .background(color = Color.Gray, CircleShape)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Straighten,
                        "RoomSize",
                        Modifier.size(16.dp),
                        tint = colors.onSurface.copy(0.5f)
                    )

                    Text(
                        "${room.roomWidth} x ${room.roomHeight} ft",
                        style = typos.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface.copy(0.5f)
                        )
                    )

                }

            }

            Text(
                "Attached Photos (${room.images.size})",
                style = typos.labelSmall.copy(
                    color = colors.onSurface.copy(0.5f)
                )
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                itemsIndexed(room.images) { index, fileKey ->
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)
//                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = "${BuildConfig.BASE_URL}files/$fileKey",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )

                        if (index == 0)
                            Text(
                                "Primary",
                                style = typos.labelSmall.copy(
                                    color = colors.onPrimary
                                ),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = Space.xs, bottom = Space.xs)
                                    .background(colors.primary, RoundedCornerShape(4.dp))
                                    .padding(Space.sm, Space.xs)
                            )

                    }
                }
            }


            AppDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onEdit) {
                    Icon(
                        Icons.Outlined.Edit, "Edit",
                        tint = colors.primary
                    )
                    Text("Edit Room")
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Outlined.Delete, "Delete",
                        tint = colors.error
                    )
                }
            }

        }

    }

    if (showDeleteDialog)
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )

}

