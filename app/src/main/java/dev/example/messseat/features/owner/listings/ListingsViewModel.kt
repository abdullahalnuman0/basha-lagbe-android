package dev.example.messseat.features.owner.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.presentation.widgets.PageLoader
import dev.example.messseat.core.utils.AppProperties
import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.data.repository.PropertyRepository
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