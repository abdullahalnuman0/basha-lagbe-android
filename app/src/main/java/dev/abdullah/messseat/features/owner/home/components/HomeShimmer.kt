package dev.abdullah.messseat.features.owner.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.widgets.ShimmerBox
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun HomeShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier.padding(Space.md),
        verticalArrangement = Arrangement.spacedBy(Space.listGap)
    ) {
        GreetingShimmer()

        AddBtnShimmer()

        OverviewShimmer()

        YourRoomsShimmer()

        ChipBtnShimmer()

        RoomItem()
        RoomItem()

    }


}


@Composable
private fun RoomItem(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp),
        )
        {

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                height = 180.dp,
                shape = RoundedCornerShape(0.dp),
            )

            Row(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(Space.listGap),
                horizontalArrangement = Arrangement.spacedBy(Space.listGap)
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.2f),
                        height = 20.dp,
                        shape = RoundedCornerShape(50),
                    )
                }

            }

            // Top-right actions
            ShimmerBox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Space.listGap)
                    .fillMaxWidth(0.2f),
                height = 20.dp,
                shape = RoundedCornerShape(50),
            )

            // bottom-start location
            ShimmerBox(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Space.listGap)
                    .fillMaxWidth(0.5f),
                height = 16.dp
            )

        }

        Column(
            Modifier.padding(Space.sm),
            verticalArrangement = Arrangement.spacedBy(Space.listGap)
        ) {
            GreetingShimmer()
        }
    }
}

@Composable
private fun ChipBtnShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Space.listGap),
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(3) {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .clip(RoundedCornerShape(8.dp)),
                height = 28.dp
            )
        }

    }
}

@Composable
private fun YourRoomsShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.2f),
            height = 20.dp
        )
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.15f),
            height = 16.dp
        )
    }
}

@Composable
private fun OverviewShimmer(modifier: Modifier = Modifier) {

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Space.listGap)
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.2f),
            height = 16.dp
        )

        repeat(2) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.listGap)
            ) {
                repeat(2) {
                    ShimmerBox(
                        modifier = modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        height = 80.dp
                    )
                }
            }
        }
    }

}

@Composable
private fun AddBtnShimmer(modifier: Modifier = Modifier) {

    ShimmerBox(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        height = 80.dp
    )

}

@Composable
private fun GreetingShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth()
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.7f),
            height = 24.dp
        )
        AppSpacer(Space.md)

        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.5f),
            height = 16.dp
        )
    }
}