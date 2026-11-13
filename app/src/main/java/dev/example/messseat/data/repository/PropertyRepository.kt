package dev.example.messseat.data.repository

import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.PropertyApi
import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.property.OwnerDashboardSummaryResponse
import dev.example.messseat.data.dto.property.PropertyRequest
import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.data.dto.property.PropertyUpdateRequest
import dev.example.messseat.data.dto.room.RoomAvailabilityRequest
import dev.example.messseat.data.dto.room.RoomRequest
import dev.example.messseat.data.dto.room.RoomResponse

class PropertyRepository(
    private val api: PropertyApi
) {

    suspend fun create(req: PropertyRequest): ApiResult<PropertyResponse> =
        safeApiCall { api.create(req) }

    suspend fun update(propertyId: Long, req: PropertyUpdateRequest): ApiResult<PropertyResponse> =
        safeApiCall { api.update(propertyId, req) }

    suspend fun findAll(params: Map<String, String>): ApiResult<PageResponse<PropertyResponse>> =
        safeApiCall { api.findAll(params) }

    suspend fun findAll(): ApiResult<List<PropertyResponse>> =
        safeApiCall { api.findAll() }

    suspend fun findOne(id: Long): ApiResult<PropertyResponse> =
        safeApiCall { api.findOne(id) }

    suspend fun deleteProperty(id: Long): ApiResult<Unit> =
        safeApiCall { api.delete(id) }

    //========================================
    //    Dashboard
    //========================================
    suspend fun getDashboardSummary(): ApiResult<OwnerDashboardSummaryResponse> =
        safeApiCall { api.getDashboardSummary() }

    //========================================
    //    Room
    //========================================

    suspend fun createRoom(propertyId: Long, body: RoomRequest): ApiResult<RoomResponse> =
        safeApiCall { api.createRoom(propertyId, body) }

    suspend fun updateRoom(roomId: Long, body: RoomRequest): ApiResult<RoomResponse> =
        safeApiCall { api.updateRoom(roomId, body) }

    suspend fun roomAvailability(body: RoomAvailabilityRequest): ApiResult<Unit> =
        safeApiCall { api.roomAvailability(body) }

}