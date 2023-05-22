package com.mealkitary.domain.reservation

import java.time.LocalDateTime

class Reservation private constructor(
    private val lineItems: List<ReservationLineItem>,
    private val shop: Shop,
    private val reserveAt: LocalDateTime
) {
    companion object {
        fun of(lineItems: List<ReservationLineItem>, shop: Shop, reserveAt: LocalDateTime): Reservation {
            return Reservation(lineItems, shop, reserveAt)
        }
    }
}
