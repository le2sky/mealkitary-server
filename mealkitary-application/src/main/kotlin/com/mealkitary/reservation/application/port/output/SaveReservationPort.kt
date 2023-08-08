package com.mealkitary.reservation.application.port.output

import com.mealkitary.reservation.domain.reservation.Reservation

interface SaveReservationPort {

    fun saveOne(reservation: Reservation): Long
}
