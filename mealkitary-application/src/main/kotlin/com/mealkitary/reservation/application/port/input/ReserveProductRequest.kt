package com.mealkitary.reservation.application.port.input

import java.time.LocalDateTime
import java.util.UUID

data class ReserveProductRequest(
    val shopId: UUID,
    val products: List<ReservedProduct>,
    val reservedAt: LocalDateTime
)
