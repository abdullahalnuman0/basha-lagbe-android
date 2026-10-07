package dev.abdullah.messseat.features.owner.home.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.typography as typos
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.features.shared.settings.components.SectionCard
import dev.abdullah.messseat.ui.theme.Space

/*
@Composable
fun AddNewListingBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.primary,
//            containerColor = colors.primary,
//            contentColor = colors.onTertiary
        ),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Space.xl, vertical = Space.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        colors.onSurface.copy(alpha = 0.45f)
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
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Add, "Add")
            }

            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Space.xs)
            ) {
                Text(
                    text = "Add New Listing",
                    style = typos.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Text(
                    text = "Post a new hostel or mess",
                    style = typos.labelLarge.copy(
                        color = LocalContentColor.current.copy(0.7f),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Icon(
                Icons.AutoMirrored.Default.ArrowForwardIos, null,
                modifier = Modifier.size(16.dp)
            )

        }
    }

}*/

@Composable
fun AddNewListingBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    /*Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer
        ),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp
        )
    )*/
    SectionCard(
        modifier = modifier, onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Space.lg,
                    vertical = Space.md
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        colors.primary.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add new listing",
                    modifier = Modifier.size(24.dp),
                    tint = colors.primary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Space.md),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Add New Listing",
                    style = typos.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Post a new hostel or mess",
                    style = typos.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.65f)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        colors.primary.copy(alpha = 0.08f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colors.primary
                )
            }
        }
    }
}
