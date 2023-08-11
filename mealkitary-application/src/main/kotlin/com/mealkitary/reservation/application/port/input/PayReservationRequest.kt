package com.mealkitary.reservation.application.port.input

import java.util.UUID

data class PayReservationRequest(
    val paymentKey: String,
    val reservationId: UUID,
    val amount: Int
)
