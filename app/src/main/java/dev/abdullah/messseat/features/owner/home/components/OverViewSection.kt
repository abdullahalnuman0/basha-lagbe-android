package dev.abdullah.messseat.features.owner.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.features.common.settings.components.SectionCard
import dev.abdullah.messseat.ui.theme.Space

data class OverViewData(
    val title: String,
    val value: Long,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OverViewSection(
    modifier: Modifier = Modifier,
    overViews: List<OverViewData>
) {

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = "Overview",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(Modifier.height(Space.sm))

        Column(
            verticalArrangement = Arrangement.spacedBy(Space.listGap)
        ) {
            overViews.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.listGap)
                ) {
                    rowItems.forEach { item ->
                        OverViewCard(
                            Modifier.weight(1f),
                            item
                        )
                    }
                }
            }
        }

    }

}

@Composable
private fun OverViewCard(
    modifier: Modifier = Modifier,
    item: OverViewData
) {

   /* Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    )*/
    SectionCard(modifier=modifier,
        contentPadding = PaddingValues(0.dp)
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Space.lg, vertical = Space.sm),
            verticalArrangement = Arrangement.spacedBy(Space.sm)
        ) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = LocalContentColor.current.copy(0.7f)
                    )
                )

                Box(
                    Modifier
                        .size(32.dp)
                        .background(item.color.copy(0.1f), CircleShape)
                        .clip(CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        item.icon, item.title,
                        tint = item.color,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                item.value.toString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = item.color
                )
            )
        }
    }

}