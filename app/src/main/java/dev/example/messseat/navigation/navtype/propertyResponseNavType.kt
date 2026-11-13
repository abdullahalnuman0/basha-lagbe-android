package dev.example.messseat.navigation.navtype

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import dev.example.messseat.data.dto.property.PropertyResponse
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

val propertyResponseNavType = object : NavType<PropertyResponse?>(
    isNullableAllowed = true
) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: PropertyResponse?
    ) {
        bundle.putString(key, value?.let { Json.encodeToString(it) })
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): PropertyResponse? {
        return bundle.getString(key)?.let {
            Json.decodeFromString<PropertyResponse>(it)
        }
    }

    override fun parseValue(value: String): PropertyResponse? {
        return Json.decodeFromString(URLDecoder.decode(value, "UTF-8"))
    }

    override fun serializeAsValue(value: PropertyResponse?): String {
        return URLEncoder.encode(
            Json.encodeToString(value),
            "UTF-8"
        )
    }
}