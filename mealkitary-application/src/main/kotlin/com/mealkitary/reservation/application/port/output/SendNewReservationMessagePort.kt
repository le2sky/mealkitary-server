package com.mealkitary.reservation.application.port.output

import java.util.UUID

interface SendNewReservationMessagePort {

    fun sendNewReservationMessage(reservationId: UUID, description: String)
}
