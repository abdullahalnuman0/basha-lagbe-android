package dev.example.messseat.data.repository

import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.TenantApi
import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.tenant.PropertyTenantResponse
import dev.example.messseat.data.dto.tenant.RoomTenantResponse
import dev.example.messseat.data.dto.tenant.SavedRoomsRequest

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

    suspend fun getSavedRooms(roomIds: List<Long>): ApiResult<List<RoomTenantResponse>> =
        safeApiCall { api.getSavedRooms(SavedRoomsRequest(roomIds)) }

}