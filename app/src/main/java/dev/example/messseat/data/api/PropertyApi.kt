package dev.example.messseat.data.api

import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.property.OwnerDashboardSummaryResponse
import dev.example.messseat.data.dto.property.PropertyRequest
import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.data.dto.property.PropertyUpdateRequest
import dev.example.messseat.data.dto.room.RoomAvailabilityRequest
import dev.example.messseat.data.dto.room.RoomRequest
import dev.example.messseat.data.dto.room.RoomResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PropertyApi {

    @POST("owner/properties")
    suspend fun create(@Body body: PropertyRequest): PropertyResponse

    @PUT("owner/properties/{id}")
    suspend fun update(
        @Path("id") propertyId: Long,
        @Body body: PropertyUpdateRequest
    ): PropertyResponse

    @GET("owner/properties")
    suspend fun findAll(@QueryMap params: Map<String, String>): PageResponse<PropertyResponse>

    @GET("owner/properties/all")
    suspend fun findAll(): List<PropertyResponse>

    @GET("owner/properties/{id}")
    suspend fun findOne(@Path("id") id: Long): PropertyResponse

    @DELETE("owner/properties/{id}")
    suspend fun delete(@Path("id") id: Long)

    //==========================================
    //    Owner Dashboard
    //==========================================
    @GET("owner/properties/dashboard/summary")
    suspend fun getDashboardSummary(): OwnerDashboardSummaryResponse

    //========================================
    //    Room
    //========================================

    @POST("owner/properties/room/{id}")
    suspend fun createRoom(@Path("id") propertyId: Long, @Body body: RoomRequest): RoomResponse

    @PUT("owner/properties/room/{id}")
    suspend fun updateRoom(@Path("id") roomId: Long, @Body body: RoomRequest): RoomResponse

    @PUT("owner/properties/room/availability")
    suspend fun roomAvailability(@Body body: RoomAvailabilityRequest)
}