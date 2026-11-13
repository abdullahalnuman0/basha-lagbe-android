package dev.example.messseat.core.presentation.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.example.messseat.R
import dev.example.messseat.core.utils.toImageUrl
import dev.example.messseat.core.utils.toMoney
import dev.example.messseat.data.enums.PropertyGender
import dev.example.messseat.data.enums.PropertyType
import dev.example.messseat.data.enums.RoomType
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.features.owner.home.components.ListingItemClip
import dev.example.messseat.features.owner.home.components.PendingAlert
import dev.example.messseat.ui.theme.Space
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun RoomItemCard(
    modifier: Modifier = Modifier,
    isOwnerView: Boolean,
    images: List<String>,
    roomNumber: String,
    propertyType: PropertyType,
    gender: PropertyGender,
    roomType: RoomType,
    rent: Int,
    location: String,
    approved: Boolean,
    title: String,
    institutionNearby: String,
    roomSize: String,
    totalSeats: Int,
    totalView: Long = 0,
    moreContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {

    var currentRoomIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(images) {
        if (images.size <= 1) return@LaunchedEffect
        while (true) {
            delay(5.seconds)
            currentRoomIndex = (currentRoomIndex + 1) % images.size
        }
    }

    val image = images.getOrNull(currentRoomIndex) ?: images[0]

    val finalTitle = buildAnnotatedString {
        append(title)
        withStyle(
            SpanStyle(
                color = colors.onSurface.copy(0.7f),
                fontSize = typos.bodyMedium.fontSize
            )
        ) {
            append("($roomNumber)")
        }
    }

    SectionCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            )
            {

                // Image  smooth transition
                AnimatedContent(
                    targetState = image,
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
                ) { ts ->
                    AsyncImage(
                        model = ts.toImageUrl(),
                        contentDescription = title,
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

                ListingItemClip(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Space.md),
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
                            text = location,
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

                if (isOwnerView && !approved) {
                    PendingAlert()
                    Spacer(Modifier.height(Space.sm))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    Text(
                        finalTitle,
                        style = typos.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )


                    if (isOwnerView) {
                        Row(
                            Modifier
                                .background(Color.LightGray, RoundedCornerShape(12.dp))
                                .padding(horizontal = Space.sm, vertical = Space.xs),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Space.xs)
                        ) {
                            Icon(
                                Icons.Outlined.RemoveRedEye,
                                "Rooms",
                                Modifier.size(16.dp),
                            )
                            Text(
                                totalView.toString(),
                                style = typos.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Space.xs))

                if (institutionNearby.isNotEmpty() && institutionNearby.length > 3) {
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
                            text = institutionNearby,
                            style = typos.labelMedium.copy(
                                color = LocalContentColor.current.copy(0.5f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(Space.sm))

                HorizontalDivider(color = colors.onBackground.copy(alpha = 0.2f))

                Spacer(Modifier.height(Space.sm))

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
                            Icons.Outlined.Straighten,
                            "Rooms",
                            Modifier.size(16.dp),
                        )
                        Text(
                            text = "$roomSize ft",
                            style = typos.labelMedium
                        )
                    }

                    VerticalDivider(Modifier.height(12.dp), color = colors.onBackground.copy(alpha = 0.2f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Space.xs)
                    ) {
                        Icon(
                            Icons.Outlined.Hotel,
                            "Rooms",
                            Modifier.size(16.dp),
                        )
                        Text(
                            text = "$totalSeats Available Seats",
                            style = typos.labelMedium
                        )
                    }
                }


                moreContent?.let {
                    Spacer(Modifier.height(Space.sm))
                    moreContent()
                }
            }


        }
    }
}

/*
@Composable
fun RoomItemCard(
    modifier: Modifier = Modifier,
    isOwnerView: Boolean,
    images: List<String>,
    roomNumber: String,
    propertyType: PropertyType,
    gender: PropertyGender,
    roomType: RoomType,
    rent: Int,
    location: String,
    approved: Boolean,
    title: String,
    institutionNearby: String,
    roomSize: String,
    totalSeats: Int,
    totalView: Long = 0,
    moreContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {

    var currentRoomIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(images) {
        if (images.size <= 1) return@LaunchedEffect
        while (true) {
            delay(5.seconds)
            currentRoomIndex = (currentRoomIndex + 1) % images.size
        }
    }

    val image = images.getOrNull(currentRoomIndex) ?: images[0]

    val finalTitle = buildAnnotatedString {
        append(title)
        withStyle(
            SpanStyle(
                color = Color.White.copy(0.7f),
                fontSize = typos.bodyMedium.fontSize
            )
        ) {
            append("($roomNumber)")
        }
    }

    AppCard(
        modifier,
        onClick,
        1.dp
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isOwnerView && !approved) 280.dp else 260.dp),
            contentAlignment = Alignment.Center
        )
        {

            // Image  smooth transition
            AnimatedContent(
                targetState = image,
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
            ) { ts ->
                AsyncImage(
                    model = ts.toImageUrl(),
                    contentDescription = title,
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

            // 2. Gradient Overlay Layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, // Top
                                Color.Transparent, // Middle-top
                                Color.Black.copy(alpha = 0.6f), // Middle-bottom
                                Color.Black.copy(alpha = 0.95f) // Bottom
                            )
                        )
                    )
            )

            // 3. Top Badges (Tags & Price)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Space.md)
            )
            {

                // Left Badges
                Row(
                    Modifier.align(Alignment.TopStart),
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

                // Right Rent Tag
                ListingItemClip(
                    modifier = Modifier.align(Alignment.TopEnd),
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

            // 4. Bottom Information Layer
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(
                        horizontal = Space.lg,
                        vertical = Space.md
                    )
            )
            {

                // Pending alert
                if (isOwnerView && !approved) {
                    PendingAlert()
                    Spacer(Modifier.height(Space.sm))
                }

                // Title and Views
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        finalTitle,
                        style = typos.headlineSmall.copy(color = Color.White),
                        modifier = Modifier.weight(1f)
                    )

                    if (isOwnerView) {
                        Row(
                            Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = Space.sm, vertical = Space.xs),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Space.xs)
                        ) {
                            Icon(
                                Icons.Outlined.RemoveRedEye,
                                contentDescription = "Rooms",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Text(
                                text = totalView.toString(),
                                style = typos.bodyMedium.copy(color = Color.White)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(Space.xs))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Space.xs)
                ) {

                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "location",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFF7043)
                    )

                    Text(
                        text = location,
                        style = typos.labelLarge.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                Spacer(Modifier.height(Space.xs))

                // Institution
                if (institutionNearby.isNotEmpty() && institutionNearby.length > 3) {
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
                            text = institutionNearby,
                            style = typos.labelMedium.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(Space.sm))

                // Divider with opacity for dark background
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))


                Spacer(Modifier.height(Space.sm))

                // Size & Seats Grid Style
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
                            Icons.Outlined.Straighten,
                            "Rooms",
                            Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "$roomSize ft",
                            style = typos.labelMedium.copy(
                                color = Color.White,
                            )
                        )
                    }

                    VerticalDivider(Modifier.height(12.dp), color = Color.White.copy(alpha = 0.2f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Space.xs)
                    ) {
                        Icon(
                            Icons.Outlined.Hotel,
                            "Rooms",
                            Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "$totalSeats Available Seats",
                            style = typos.labelMedium.copy(
                                color = Color.White,
                            )
                        )
                    }
                }


                moreContent?.let {
                    Spacer(Modifier.height(Space.sm))
                    moreContent()
                }
            }

        }

    }
}*/
