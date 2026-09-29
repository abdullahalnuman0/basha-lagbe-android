package dev.abdullah.messseat.data.mapper

import dev.abdullah.messseat.data.enums.PropertyType as Data
import dev.abdullah.messseat.domain.enums.PropertyType as Domain

//fun Data.toDomain() = when (this) {
//    Data.HOSTEL -> Domain.HOSTEL
//    Data.FLAT -> Domain.FLAT
//    Data.MESS -> Domain.MESS
//    Data.SUBLET -> Domain.SUBLET
//}

fun Data.toDomain() = Domain.valueOf(this.name)

fun Domain.toData() = Data.valueOf(this.name)