package dev.abdullah.messseat.data.repository

import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.network.safeApiCall
import dev.abdullah.messseat.data.api.TenantApi
import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.tenant.PropertyTenantResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse

class TenantRepository(
    private val api: TenantApi
) {

    suspend fun findRooms(params: Map<String, String?>): ApiResult<PageResponse<RoomTenantResponse>> =
        safeApiCall { api.findRooms(params) }

    suspend fun findRoom(id: Long): ApiResult<RoomTenantResponse> =
        safeApiCall { api.findRoom(id) }

    suspend fun findProperties(params: Map<String, String?>): ApiResult<PageResponse<PropertyTenantResponse>> =
        safeApiCall { api.findProperties(params) }


    suspend fun findProperty(id: Long): ApiResult<PropertyTenantResponse> =
        safeApiCall { api.findProperty(id) }

    suspend fun updateRoomView(id: Long) =
        safeApiCall { api.updateRoomView(id) }

}