package dev.abdullah.messseat.features.tenant.room_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardBackspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.LocalSnackbar
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.features.owner.property_details.components.PropertyAbout
import dev.abdullah.messseat.features.owner.property_details.components.PropertyFacilities
import dev.abdullah.messseat.features.tenant.room_details.components.HeaderSection
import dev.abdullah.messseat.features.tenant.room_details.components.OwnerInfoCard
import dev.abdullah.messseat.features.tenant.room_details.components.RoomBasicInfo
import dev.abdullah.messseat.navigation.SharedRoute
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun RoomDetailsScreen(
    modifier: Modifier = Modifier,
    room: RoomTenantResponse,
    onNavigate: (NavRoute) -> Unit,
    onBack: () -> Unit,
    vm: RoomDetailsViewModel = hiltViewModel()
) {

    LaunchedEffect(room) {
        vm.updateRoomView(room.roomId)
    }

    val isFavorite by vm.isFavorite.collectAsStateWithLifecycle()
    val isChatOpening by vm.isChatOpening.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    Box(modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .zIndex(1f)
                .padding(start = Space.xs, top = Space.xs)
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
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
                )
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardBackspace,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }



        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {

            HeaderSection(
                images = room.images,
                isSaved = isFavorite,
                propertyType = room.propertyType,
                gender = room.gender,
                roomType = room.roomType,
                rent = room.rent,
                onFavorite = {
                    vm.updateFavoriteState {
                        snackbar.showWithAction(if (it) "Save post" else "Remove post")
                    }
                }
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(Space.screen),
                verticalArrangement = Arrangement.spacedBy(Space.listGap)
            ) {

                RoomBasicInfo(
                    title = room.title,
                    roomNumber = room.roomNumber,
                    location = room.address,
                    institutionNearby = room.institutionNearby,
                    roomSize = "${room.roomWidth} x ${room.roomHeight}",
                    totalSeats = room.availableSeats
                )

                OwnerInfoCard(
                    name = room.ownerName,
                    avatarUrl = room.ownerAvatarUrl,
                    isChatOpening = isChatOpening,
                    onCall = {},
                    onChat = {
                        vm.openChat(
                            onChat = { onNavigate(SharedRoute.Conversation(it)) },
                            onError = { snackbar.showWithAction(it) })
                    }
                )

                PropertyFacilities(
                    facilities = room.facilities,
                    shape = RoundedCornerShape(16.dp)
                )

                if (room.description.isNotEmpty())
                    PropertyAbout(
                        desc = room.description,
                        shape = RoundedCornerShape(16.dp)
                    )

            }

        }

    }

}