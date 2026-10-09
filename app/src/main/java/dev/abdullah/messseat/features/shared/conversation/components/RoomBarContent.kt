package dev.abdullah.messseat.features.shared.conversation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.features.shared.settings.components.SectionCard
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun RoomBarContent(
    modifier: Modifier = Modifier,
    room: RoomTenantResponse,
    onClick: () -> Unit
) {
    SectionCard(
        modifier
            .padding(horizontal = Space.sm)
            .padding(top = Space.sm),
        PaddingValues(0.dp),
        onClick = onClick
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
    }
}