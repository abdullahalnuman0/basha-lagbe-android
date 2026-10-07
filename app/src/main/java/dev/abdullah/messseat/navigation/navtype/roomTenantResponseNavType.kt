package dev.abdullah.messseat.navigation.navtype

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

val roomTenantResponseNavType = object : NavType<RoomTenantResponse>(
    isNullableAllowed = false
) {
    override fun put(bundle: SavedState, key: String, value: RoomTenantResponse) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): RoomTenantResponse? {
        return bundle.getString(key)?.let {
            Json.decodeFromString<RoomTenantResponse>(it)
        }
    }

    override fun parseValue(value: String): RoomTenantResponse {
        return Json.decodeFromString(URLDecoder.decode(value, "UTF-8"))
    }

    override fun serializeAsValue(value: RoomTenantResponse): String {
        return URLEncoder.encode(
            Json.encodeToString(value),
            "UTF-8"
        )
    }
}