package com.mealkitary.domain.reservation

import java.time.LocalDateTime

class Reservation private constructor(
    private val lineItems: List<ReservationLineItem>,
    private val shop: Shop,
    private val reserveAt: LocalDateTime
) {
    companion object {
        fun of(lineItems: List<ReservationLineItem>, shop: Shop, reserveAt: LocalDateTime): Reservation {
            checkLineItemsAtLeastOne(lineItems)
            checkBeforeTime(reserveAt)
            return Reservation(lineItems, shop, reserveAt)
        }

        private fun checkLineItemsAtLeastOne(lineItems: List<ReservationLineItem>) {
            if (lineItems.isEmpty()) {
                throw IllegalArgumentException("예약 상품은 적어도 한 개 이상이어야 합니다.")
            }
        }

        private fun checkBeforeTime(reserveAt: LocalDateTime) {
            if (reserveAt.isBefore(LocalDateTime.now())) {
                throw IllegalStateException("예약 시간은 적어도 미래 시점이어야 합니다.")
            }
        }
    }
}
