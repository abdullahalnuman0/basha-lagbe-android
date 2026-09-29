package dev.abdullah.messseat.data.mapper

import dev.abdullah.messseat.domain.enums.PropertyGender as Domain
import dev.abdullah.messseat.data.enums.PropertyGender as Data

fun Data.toDomain() = Domain.valueOf(this.name)

fun Domain.toData() = Data.valueOf(this.name)