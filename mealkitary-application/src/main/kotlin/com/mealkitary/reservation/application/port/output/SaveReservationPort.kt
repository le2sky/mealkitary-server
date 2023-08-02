package com.mealkitary.reservation.application.port.output

import com.mealkitary.reservation.domain.Reservation

interface SaveReservationPort {

    fun saveOne(reservation: Reservation): Long
}
