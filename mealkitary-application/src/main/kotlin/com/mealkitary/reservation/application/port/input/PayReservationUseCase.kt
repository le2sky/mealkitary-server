package com.mealkitary.reservation.application.port.input

import java.util.UUID

interface PayReservationUseCase {

    fun pay(payReservationRequest: PayReservationRequest): UUID
}
