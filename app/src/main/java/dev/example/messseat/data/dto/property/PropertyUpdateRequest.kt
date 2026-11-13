package dev.example.messseat.data.dto.property

import dev.example.messseat.data.enums.PropertyGender
import dev.example.messseat.data.enums.PropertyType
import kotlinx.serialization.Serializable

@Serializable
data class PropertyUpdateRequest(
    val address: String,
    val area: String,
    val contactNumber: String,
    val description: String,
    val district: String,
    val division: String,
    val facilities: List<String>,
    val gender: PropertyGender,
    val institutionNearby: String?=null,
    val title: String,
    val type: PropertyType
)