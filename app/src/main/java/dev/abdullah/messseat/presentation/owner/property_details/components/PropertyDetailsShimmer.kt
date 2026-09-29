package dev.abdullah.messseat.presentation.owner.property_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppCard
import dev.abdullah.messseat.core.presentation.widgets.ShimmerBox
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun PropertyDetailsShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Space.listGap),
    ) {

        // ---------------------------------------------------------
        // Property image
        // ---------------------------------------------------------
        PropertyImageShimmer()

        // ---------------------------------------------------------
        // Property information
        // ---------------------------------------------------------
        PropertyHeaderShimmer()

        // ---------------------------------------------------------
        // Statistics
        // ---------------------------------------------------------
        PropertyStatsShimmer()

        // ---------------------------------------------------------
        // About this property
        // ---------------------------------------------------------
        PropertyAboutShimmer()

        // ---------------------------------------------------------
        // Facilities & amenities
        // ---------------------------------------------------------
        PropertyFacilitiesShimmer()

        Spacer(
            modifier = Modifier.height(Space.lg),
        )
    }
}


/**
 * Large property image area + gallery controls.
 */
@Composable
private fun PropertyImageShimmer(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
    ) {

        ShimmerBox(
            modifier = Modifier.fillMaxWidth(),
            height = 220.dp,
            shape = RoundedCornerShape(0.dp),
        )

        // Top-right action buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Space.md),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            ShimmerBox(
                modifier = Modifier.size(38.dp),
                height = 38.dp,
                shape = RoundedCornerShape(50),
            )

            ShimmerBox(
                modifier = Modifier.size(38.dp),
                height = 38.dp,
                shape = RoundedCornerShape(50),
            )
        }

        // Gallery button
        ShimmerBox(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Space.md)
                .fillMaxWidth(0.27f),
            height = 30.dp,
            shape = RoundedCornerShape(50),
        )

        // Image counter
        ShimmerBox(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Space.md)
                .fillMaxWidth(0.18f),
            height = 24.dp,
            shape = RoundedCornerShape(50),
        )
    }
}


/**
 * Property name, location, type/status chips and map area.
 */
@Composable
private fun PropertyHeaderShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.sm),
    ) {

        // Property title + approval badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    height = 20.dp,
                    shape = MaterialTheme.shapes.small,
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.42f),
                    height = 20.dp,
                    shape = MaterialTheme.shapes.small,
                )
            }

            ShimmerBox(
                modifier = Modifier
                    .padding(start = Space.sm)
                    .fillMaxWidth(0.27f),
                height = 26.dp,
                shape = RoundedCornerShape(50),
            )
        }

        // Location / map
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Space.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShimmerBox(
                    modifier = Modifier.size(18.dp),
                    height = 18.dp,
                    shape = RoundedCornerShape(50),
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.55f),
                    height = 14.dp,
                )
            }

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.18f),
                height = 14.dp,
            )
        }

        // Property type / gender / rent chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            PropertyChipShimmer(width = 70.dp)
            PropertyChipShimmer(width = 60.dp)
            PropertyChipShimmer(width = 75.dp)
        }
    }
}


/**
 * Single small chip placeholder.
 */
@Composable
private fun PropertyChipShimmer(
    width: androidx.compose.ui.unit.Dp,
) {
    ShimmerBox(
        modifier = Modifier.size(
            width = width,
            height = 25.dp,
        ),
        height = 25.dp,
        shape = RoundedCornerShape(50),
    )
}


/**
 * Four statistics cards:
 *
 * Total Rooms
 * Available Seats
 * Total Capacity
 * Key Facilities
 */
@Composable
private fun PropertyStatsShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            PropertyStatCardShimmer(
                modifier = Modifier.weight(1f),
            )

            PropertyStatCardShimmer(
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            PropertyStatCardShimmer(
                modifier = Modifier.weight(1f),
            )

            PropertyStatCardShimmer(
                modifier = Modifier.weight(1f),
            )
        }
    }
}


/**
 * Individual statistic card.
 */
@Composable
private fun PropertyStatCardShimmer(
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Space.md),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            // Icon
            ShimmerBox(
                modifier = Modifier.size(34.dp),
                height = 34.dp,
                shape = RoundedCornerShape(10.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                // Number
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.55f),
                    height = 18.dp,
                )

                // Label
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    height = 11.dp,
                )
            }
        }
    }
}


/**
 * About this property section.
 */
@Composable
private fun PropertyAboutShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.sm),
    ) {

        // Section title
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.45f),
            height = 19.dp,
            shape = MaterialTheme.shapes.small,
        )

        Spacer(
            modifier = Modifier.height(2.dp),
        )

        // Description lines
        ShimmerBox(
            modifier = Modifier.fillMaxWidth(),
            height = 12.dp,
        )

        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.95f),
            height = 12.dp,
        )

        ShimmerBox(
            modifier = Modifier.fillMaxWidth(0.72f),
            height = 12.dp,
        )
    }
}


/**
 * Facilities & Amenities section.
 */
@Composable
private fun PropertyFacilitiesShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.sm),
    ) {

        // Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.55f),
                height = 19.dp,
                shape = MaterialTheme.shapes.small,
            )

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.18f),
                height = 11.dp,
            )
        }

        Spacer(
            modifier = Modifier.height(2.dp),
        )

        // Facility row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )

            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )
        }

        // Facility row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )

            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )
        }

        // Facility row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )

            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )
        }

        // Facility row 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )

            FacilityItemShimmer(
                modifier = Modifier.weight(1f),
            )
        }

        // More amenities
        ShimmerBox(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.35f),
            height = 14.dp,
        )
    }
}


/**
 * Single facility placeholder.
 */
@Composable
private fun FacilityItemShimmer(
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Space.sm,
                    vertical = 10.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(Space.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            ShimmerBox(
                modifier = Modifier.size(18.dp),
                height = 18.dp,
                shape = RoundedCornerShape(50),
            )

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.65f),
                height = 12.dp,
            )
        }
    }
}