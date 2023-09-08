package com.mealkitary.firebase.message

import java.util.UUID

data class ReservationCreatedMessage(
    val title: String,
    val description: String,
    val reservationId: UUID,
    val token: String
)
