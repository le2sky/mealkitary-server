package com.mealkitary.reservation.application.port.input

import java.time.LocalDateTime
import java.util.UUID

data class ReservationResponse(
    val reservationId: UUID,
    val shopName: String,
    val description: String,
    val reserveAt: LocalDateTime,
    val status: String,
    val reservedProduct: List<ReservedProduct>
)
