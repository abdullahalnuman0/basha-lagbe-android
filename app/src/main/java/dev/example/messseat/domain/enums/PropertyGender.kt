package dev.example.messseat.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Male
import androidx.compose.ui.graphics.vector.ImageVector

enum class PropertyGender(
    val title: String,
    val icon: ImageVector,
) {
    MALE(
        title = "Male",
        icon = Icons.Outlined.Male,
    ),

    FEMALE(
        title = "Female",
        icon = Icons.Outlined.Female,
    ),

    ANY(
        title = "Any",
        icon = Icons.Outlined.Groups,
    )
}