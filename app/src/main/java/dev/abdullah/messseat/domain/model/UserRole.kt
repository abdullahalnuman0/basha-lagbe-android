package dev.abdullah.messseat.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class UserRole(
    val title: String,
    val subTitle: String,
    val desc: String,
    val icon: ImageVector
) {
    TENANT(
        "Find a place", "Tenant / Mess Member",
        "Find a home or mess that matches your needs and budget in seconds.",
        Icons.Default.Search
    ),
    OWNER(
        "Rent out a place", "Owner / Mess Manager",
        "List your home or mess, manage applicants, and find reliable tenants.",
        Icons.Default.Key
    ),
}
