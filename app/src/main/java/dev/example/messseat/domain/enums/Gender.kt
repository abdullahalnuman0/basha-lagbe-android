package dev.example.messseat.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.ui.graphics.vector.ImageVector

enum class Gender(val label: String,val icon: ImageVector) {
    MALE("Male", Icons.Outlined.Male),
    FEMALE("Female", Icons.Outlined.Female)
}