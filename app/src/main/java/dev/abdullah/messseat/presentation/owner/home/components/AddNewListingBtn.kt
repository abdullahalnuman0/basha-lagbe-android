package dev.abdullah.messseat.presentation.owner.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography as typos
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.ui.theme.Space

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
//                    .background(colors.onBackground.copy(0.5f), CircleShape),
                    .background(Color.DarkGray.copy(0.3f), CircleShape),
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

}