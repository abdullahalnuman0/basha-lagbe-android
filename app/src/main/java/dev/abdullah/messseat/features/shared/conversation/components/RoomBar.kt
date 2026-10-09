package dev.abdullah.messseat.features.shared.conversation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.features.shared.settings.components.SectionCard
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun RoomBar(
    modifier: Modifier = Modifier,
    room: RoomTenantResponse,
) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    AnimatedContent(
        targetState = expanded
    ) { exp ->

        if (!exp) {
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(start = Space.xs, top = Space.xs)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.55f),
                                Color.White.copy(alpha = 0.12f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .clickable { expanded = !expanded },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ExpandLess,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(90f)
                )
            }
        } else {
            RoomBarContent(room = room) { expanded != expanded }
            /*SectionCard(
                modifier
                    .padding(horizontal = Space.sm)
                    .padding(top = Space.sm),
                PaddingValues(0.dp),
                onClick = { expanded = !expanded }
            )
            {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(Space.md),
                    horizontalArrangement = Arrangement.spacedBy(Space.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = room.images[0].toImageUrl(),
                        contentDescription = room.title,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = rememberAsyncImagePainter(
                            R.drawable.room_default_image
                        ),
                        error = rememberAsyncImagePainter(
                            R.drawable.room_default_image
                        )
                    )

                    Column {

                        Text(
                            room.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                room.roomNumber,
                                Modifier.weight(1f),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                            )

                            Text(
                                room.rent.toMoney(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                            )
                        }
                    }
                }
            }*/
        }
    }
}