package dev.abdullah.messseat.data.mapper

import dev.abdullah.messseat.domain.enums.Gender as DomainGender
import dev.abdullah.messseat.data.enums.Gender as DataGender

// --- Data -> Domain ---
fun DataGender.toDomain() = when (this) {
    DataGender.MALE -> DomainGender.MALE
    DataGender.FEMALE -> DomainGender.FEMALE
}

// --- Domain -> Data ---
fun DomainGender.toData() = when (this) {
    DomainGender.MALE -> DataGender.MALE
    DomainGender.FEMALE -> DataGender.FEMALE
}