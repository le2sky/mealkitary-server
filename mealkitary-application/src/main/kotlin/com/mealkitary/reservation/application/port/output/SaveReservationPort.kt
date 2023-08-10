package com.mealkitary.reservation.application.port.output

import com.mealkitary.reservation.domain.reservation.Reservation
import java.util.UUID

interface SaveReservationPort {

    fun saveOne(reservation: Reservation): UUID
}
