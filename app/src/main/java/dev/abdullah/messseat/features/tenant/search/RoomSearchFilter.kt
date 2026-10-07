package dev.abdullah.messseat.features.tenant.search


import dev.abdullah.messseat.data.enums.RoomType

data class RoomSearchFilter(
    val keyword: String = "",

    val division: String = "",
    val district: String = "",
    val area: String = "",

    val roomType: RoomType? = null,

    val minRent: String = "",
    val maxRent: String = "",

    val minAvailableSeats: String = "",
    val maxAvailableSeats: String = "",

    val minRoomWidth: String = "",
    val minRoomHeight: String = "",

    val propertyType: String? = null,
    val gender: String? = null,

    val sortBy: SortOption = SortOption.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC
) {
    fun activeFilterCount(): Int {
        var count = 0

        if (division.isNotBlank()) count++
        if (district.isNotBlank()) count++
        if (area.isNotBlank()) count++
        if (roomType != null) count++
        if (minRent.isNotBlank()) count++
        if (maxRent.isNotBlank()) count++
        if (minAvailableSeats.isNotBlank()) count++
        if (maxAvailableSeats.isNotBlank()) count++
        if (minRoomWidth.isNotBlank()) count++
        if (minRoomHeight.isNotBlank()) count++
        if (propertyType != null) count++
        if (gender != null) count++

        if (sortBy != SortOption.CREATED_AT || sortOrder != SortOrder.DESC) {
            count++
        }

        return count
    }
}

enum class SortOption(
    val apiValue: String,
    val title: String
) {
    CREATED_AT("createdAt", "Newest"),
    RENT("rent", "Rent"),
    AVAILABLE_SEATS("availableSeats", "Available seats")
}

enum class SortOrder(
    val apiValue: String,
    val title: String
) {
    ASC("asc", "Low to high"),
    DESC("desc", "High to low")
}