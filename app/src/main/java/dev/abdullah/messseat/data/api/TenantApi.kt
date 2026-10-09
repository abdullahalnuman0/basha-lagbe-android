package dev.abdullah.messseat.data.api

import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.tenant.PropertyTenantResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.data.dto.tenant.SavedRoomsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TenantApi {

    @GET("tenant/rooms")
    suspend fun findRooms(@QueryMap params: Map<String, String?>): PageResponse<RoomTenantResponse>

    @GET("tenant/rooms/{roomId}")
    suspend fun findRoom(@Path("roomId") id: Long): RoomTenantResponse

    @GET("tenant/properties")
    suspend fun findProperties(@QueryMap params: Map<String, String?>): PageResponse<PropertyTenantResponse>


    @GET("tenant/properties/{propertyId}")
    suspend fun findProperty(@Path("propertyId") id: Long): PropertyTenantResponse

    @PUT("tenant/rooms/{roomId}")
    suspend fun updateRoomView(@Path("roomId") id: Long)

    @POST("tenant/rooms/saved")
    suspend fun getSavedRooms(@Body body: SavedRoomsRequest): List<RoomTenantResponse>
}
