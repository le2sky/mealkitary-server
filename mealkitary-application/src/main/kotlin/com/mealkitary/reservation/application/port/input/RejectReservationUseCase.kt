package com.mealkitary.reservation.application.port.input

import java.util.UUID

interface RejectReservationUseCase {

    fun reject(reservationId: UUID)
}
