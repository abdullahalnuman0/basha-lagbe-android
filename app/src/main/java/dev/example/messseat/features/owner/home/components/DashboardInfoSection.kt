package dev.example.messseat.features.owner.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Hotel
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.MeetingRoom
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.example.messseat.core.presentation.widgets.PageHeader
import dev.example.messseat.core.utils.greeting
import dev.example.messseat.data.dto.property.OwnerDashboardSummaryResponse
import dev.example.messseat.ui.theme.Space

@Composable
fun DashboardInfoSection(
    modifier: Modifier = Modifier,
    userName: String?,
    summary: OwnerDashboardSummaryResponse?,
    sortPosition: Int,
    onAddListing: () -> Unit,
    onSortPositionChange:(Int) -> Unit
) {


    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Space.sm)
    ) {


        Spacer(Modifier)

        PageHeader(
            title = greeting(userName),
            subtitle = "Manage your rental listings",
            titleStyle = MaterialTheme.typography.headlineMedium,
            subtitleStyle = MaterialTheme.typography.bodyMedium,
        )

        AddNewListingBtn(
            onClick = onAddListing
        )


        OverViewSection(
            overViews = listOf(
                OverViewData(
                    "Active Listings",
                    summary?.activeListings ?: 0,
                    Icons.Rounded.CheckCircleOutline,
                    colors.primary
                ),
                OverViewData(
                    "Pending Approval",
                    summary?.pendingApproval ?: 0,
                    Icons.Rounded.HourglassEmpty,
                    colors.error
                ),
                OverViewData(
                    "Available Seats",
                    summary?.availableSeats ?: 0,
                    Icons.Rounded.Hotel,
                    colors.secondary
                ),
                OverViewData(
                    "Total Rooms",
                    summary?.totalRoom ?: 0,
                    Icons.Rounded.MeetingRoom,
                    colors.tertiary
                ),
            )
        )


        TotalListingLabel(total = summary?.totalRoom ?: 0)



        FeedTabs(
            totalRoom = summary!!.totalRoom,
            approved = summary.approvedRoom,
            pending = summary.pendingRoom,
            selected = sortPosition,
            onSelect = onSortPositionChange
        )


    }

}