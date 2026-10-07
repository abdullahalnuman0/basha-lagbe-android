package dev.abdullah.messseat.features.tenant.room_details.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardBackspace
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.enums.PropertyGender
import dev.abdullah.messseat.data.enums.PropertyType
import dev.abdullah.messseat.data.enums.RoomType
import dev.abdullah.messseat.features.owner.home.components.ListingItemClip
import dev.abdullah.messseat.ui.theme.Space
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    images: List<String>,
    isSaved: Boolean = false,
    propertyType: PropertyType,
    gender: PropertyGender,
    roomType: RoomType,
    rent: Int,
    onFavorite: () -> Unit
) {
    var currentRoomIndex by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(images) {
        if (images.size <= 1) return@LaunchedEffect
        while (true) {
            delay(5.seconds)
            currentRoomIndex = (currentRoomIndex + 1) % images.size
        }
    }
    val imageUrl = (if (images.size <= 1) images[0] else images.getOrNull(currentRoomIndex)
        ?: images[0]).toImageUrl()

    Box(
        modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Image  smooth transition
        AnimatedContent(
            targetState = imageUrl,
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
        ) { url ->
            AsyncImage(
                model = url,
                contentDescription = imageUrl,
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

        // 2: Save button
        Box(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopEnd)
                .padding(end = Space.xs, top = Space.xs)
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    colors.surface.copy(alpha = 0.45f)
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
                .clickable { onFavorite() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSaved) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Back",
                tint = colors.onSurface
            )
        }

        // 3: Info for room
        Row(
            Modifier.align(Alignment.BottomStart)
                .padding(Space.sm),
            horizontalArrangement = Arrangement.spacedBy(Space.xs)
        ) {

            ListingItemClip(
                label = propertyType.name,
                icon = Icons.Default.Hotel
            )

            ListingItemClip(
                label = roomType.name,
                bg = colors.secondary,
                onBg = colors.onSecondary
            )

            ListingItemClip(
                label = gender.name,
                bg = colors.tertiary,
                onBg = colors.onTertiary
            )
        }

        ListingItemClip(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Space.sm),
            label = "${rent.toMoney()}/${
                if (propertyType in listOf(
                        PropertyType.MESS,
                        PropertyType.HOSTEL
                    )
                ) "seat" else "month"
            }",
            bg = Color.White,
            onBg = colors.primary
        )
    }

}