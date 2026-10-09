package dev.abdullah.messseat.features.shared.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.data.repository.NotificationRepository
import dev.abdullah.messseat.data.repository.UnreadCountRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notyRepo: NotificationRepository,
    private val unreadRepo: UnreadCountRepository,
) : ViewModel() {

    val loader = PageLoader(viewModelScope) { page -> notyRepo.notifications(page) }

    init {
        loader.loadFirst()
    }

    fun markRead(id: Long) = viewModelScope.launch {
        notyRepo.markNotificationRead(id)
        unreadRepo.decrementUnreadNotyCount()
        loader.mutate { list -> list.map { if (it.id == id) it.copy(read = true) else it } }
    }

    fun markAll(onResult: (String) -> Unit) = viewModelScope.launch {
        when (val r = notyRepo.markAllNotificationsRead()) {
            is ApiResult.Success -> {
                loader.mutate { list -> list.map { it.copy(read = true) } }
                onResult("Marked all as read.")
                unreadRepo.updateNotificationCount()
            }

            is ApiResult.Error -> onResult(r.message)
        }
    }

}