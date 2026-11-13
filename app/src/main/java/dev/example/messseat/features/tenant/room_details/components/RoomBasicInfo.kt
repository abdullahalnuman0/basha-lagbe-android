package dev.example.messseat.features.tenant.room_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.example.messseat.ui.theme.Space

@Composable
fun RoomBasicInfo(
    modifier: Modifier = Modifier,
    title: String,
    roomNumber: String,
    location: String,
    institutionNearby: String,
    roomSize: String,
    totalSeats: Int,
) {

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

    Column(
        modifier
            .fillMaxWidth()
    ) {

        Text(
            finalTitle,
            style = typos.headlineLarge,
        )
        Spacer(Modifier.height(Space.xl))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.xs)
        ) {

            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = "location",
                modifier = Modifier.size(16.dp),
                tint = Color.Red
            )
            Text(
                text = location,
                style = typos.labelLarge.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(Modifier.height(Space.xs))

        if (institutionNearby.isNotEmpty() && institutionNearby.length > 3) {
//        if (true) {
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

    }

}