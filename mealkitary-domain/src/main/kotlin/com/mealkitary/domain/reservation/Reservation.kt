package com.mealkitary.domain.reservation

import java.time.LocalDateTime

class Reservation private constructor(
    private val lineItems: List<ReservationLineItem>,
    private val shop: Shop,
    private val reserveAt: LocalDateTime
) {
    fun reserve() {
        if (shop.isInvalid()) {
            throw IllegalArgumentException("유효하지 않은 가게입니다.")
        }
        if (!shop.isReservableAt(reserveAt)) {
            throw IllegalArgumentException("예약하시려는 가게는 해당 시간에 예약을 받지 않습니다.")
        }
    }

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
