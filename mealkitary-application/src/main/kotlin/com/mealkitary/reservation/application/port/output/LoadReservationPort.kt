package com.mealkitary.reservation.application.port.output

import com.mealkitary.reservation.application.port.input.ReservationResponse
import com.mealkitary.reservation.domain.reservation.Reservation
import java.util.UUID

interface LoadReservationPort {

    fun loadOneReservationById(reservationId: UUID): Reservation

    fun queryOneReservationById(reservationId: UUID): ReservationResponse
}
