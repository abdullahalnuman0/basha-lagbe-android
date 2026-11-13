package dev.example.messseat.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.ui.graphics.vector.ImageVector

enum class PropertyType(
    val title: String,
    val icon: ImageVector,
) {
    HOSTEL(
        title = "Hostel",
        icon = Icons.Outlined.Hotel,
    ),

    FLAT(
        title = "Flat",
        icon = Icons.Outlined.Apartment,
    ),

    MESS(
        title = "Mess",
        icon = Icons.Outlined.Groups,
    ),

    SUBLET(
        title = "Sublet",
        icon = Icons.Outlined.HomeWork,
    ),
}