package dev.example.messseat.data.mapper

import dev.example.messseat.domain.enums.PropertyGender as Domain
import dev.example.messseat.data.enums.PropertyGender as Data

fun Data.toDomain() = Domain.valueOf(this.name)

fun Domain.toData() = Data.valueOf(this.name)