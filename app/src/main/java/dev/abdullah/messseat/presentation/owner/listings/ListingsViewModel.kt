package dev.abdullah.messseat.presentation.owner.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.core.utils.AppProperties
import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import dev.abdullah.messseat.data.repository.PropertyRepository
import javax.inject.Inject

@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val propertyRepo: PropertyRepository
) : ViewModel() {

    val properties = PageLoader<PropertyResponse>(viewModelScope) { page -> loadProperty(page) }


    init {
        properties.loadFirst()
    }

    private suspend fun loadProperty(page: Int): ApiResult<PageResponse<PropertyResponse>> {
        val params = mapOf(
            "page" to page.toString(),
            "size" to AppProperties.FEED_PAGE_SIZE.toString()
        )
        return propertyRepo.findAll(params)
    }

}