package com.mealkitary.reservation.application.port.input

import java.util.UUID

interface GetReservationQuery {

    fun loadOneReservationById(reservationId: UUID): ReservationResponse

    fun loadAllReservationByShopId(shopId: Long): List<ReservationResponse>
}
