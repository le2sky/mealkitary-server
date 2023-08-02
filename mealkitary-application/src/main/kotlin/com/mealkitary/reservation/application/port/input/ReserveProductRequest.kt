package com.mealkitary.reservation.application.port.input

import java.time.LocalDateTime

data class ReserveProductRequest(
    val shopId: Long,
    val products: List<ReservedProduct>,
    val reservedAt: LocalDateTime
)
