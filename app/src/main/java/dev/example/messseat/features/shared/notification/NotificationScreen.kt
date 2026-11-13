package dev.example.messseat.features.shared.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.example.messseat.core.presentation.components.LocalSnackbar
import dev.example.messseat.core.presentation.components.PullToRefresh
import dev.example.messseat.core.presentation.widgets.AppTopBar
import dev.example.messseat.core.presentation.widgets.EmptyBox
import dev.example.messseat.core.presentation.widgets.ErrorBox
import dev.example.messseat.core.presentation.widgets.ShimmerList
import dev.example.messseat.core.presentation.widgets.UiState
import dev.example.messseat.core.utils.timeAgo
import dev.example.messseat.data.dto.notification.NotificationResponse
import dev.example.messseat.data.enums.NotyRefType
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.navigation.NavRoute
import dev.example.messseat.navigation.OwnerRoute
import dev.example.messseat.navigation.SharedRoute
import dev.example.messseat.navigation.TenantRoute
import dev.example.messseat.ui.theme.Space

fun target(noty: NotificationResponse): NavRoute? = when (noty.refType) {
    NotyRefType.CHAT -> noty.refId?.let { SharedRoute.Conversation(it) }
    NotyRefType.OWNER_POST -> noty.refId?.let { OwnerRoute.Details(it) }
    NotyRefType.TENANT_POST -> noty.refId?.let { TenantRoute.RoomDetails(it) }
    else -> null
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    nav: NavController,
    vm: NotificationViewModel = hiltViewModel()
) {
    val loaderState by vm.loader.state.collectAsStateWithLifecycle()
    val refreshing by vm.loader.refreshing.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    Column(modifier.fillMaxSize()) {
        AppTopBar(
            title = "Notifications",
            subTitle = "Applications, messages, posts",
            onBack = nav::popBackStack,
            actions = {
                IconButton(onClick = { vm.markAll { snackbar.show(it) } }) {
                    Icon(Icons.Filled.DoneAll, "Mark all as read")
                }
            },
        )


        // Main content
        when (val s = loaderState) {
            UiState.Loading -> ShimmerList()

            is UiState.Error -> ErrorBox(
                message = s.message,
                onRetry = vm.loader::loadFirst
            )

            is UiState.Success -> PullToRefresh(refreshing, vm.loader::refresh) {

                val chats = s.data

                if (chats.isEmpty()) {
                    EmptyBox(
                        title = "You're all caught up",
                        subtitle = "Notifications about your posts, applications and contracts land here.",
                        icon = Icons.Filled.Notifications,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = Space.screen,
                            end = Space.screen,
                            top = Space.md,
                            bottom = Space.listBottom
                        ),
                        verticalArrangement = Arrangement.spacedBy(Space.listGap)
                    ) {

                        items(chats, key = { it.id }) { n ->

                            SectionCard(
                                contentPadding = PaddingValues(0.dp),
                                onClick = {
                                    if (!n.read) vm.markRead(n.id)
                                    target(n)?.let { nav.navigate(it) }
                                }
                            ) {

                                Row(
                                    Modifier
                                        .padding(Space.lg)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    // Unread marker — a dot reads faster than a bolder weight.
                                    Box(
                                        Modifier
                                            .padding(top = 6.dp, end = Space.md)
                                            .size(8.dp)
                                            .background(
                                                if (n.read) {
                                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                                } else {
                                                    MaterialTheme.colorScheme.primary
                                                },
                                                CircleShape,
                                            ),
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(n.title, style = MaterialTheme.typography.titleSmall)
                                        if (!n.body.isNullOrBlank()) {
                                            Text(
                                                n.body,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(top = Space.xs),
                                            )
                                        }
                                    }
                                    Text(
                                        timeAgo(n.createdAt),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(start = Space.sm),
                                    )
                                }
                            }

                        }

                    }
                }

            }
        }
    }

}