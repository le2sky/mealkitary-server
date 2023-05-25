package com.mealkitary.common

import com.mealkitary.common.ShopTestData.Companion.defaultShop
import com.mealkitary.reservation.domain.Reservation
import com.mealkitary.reservation.domain.ReservationLineItem
import com.mealkitary.reservation.domain.ReservationStatus
import com.mealkitary.shop.domain.Shop
import java.time.LocalDateTime

class ReservationTestData {

    class ReservationBuilder(
        private var reservationStatus: ReservationStatus = ReservationStatus.NOTPAID,
        private var lineItems: List<ReservationLineItem> = listOf(ReservationLineItem()),
        private var shop: Shop = defaultShop().build(),
        private var reserveAt: LocalDateTime = LocalDateTime.now().plusDays(1)
    ) {
        fun withReservationStatus(status: ReservationStatus): ReservationBuilder {
            this.reservationStatus = status
            return this
        }

        fun withLineItems(vararg items: ReservationLineItem): ReservationBuilder {
            this.lineItems = items.toList()
            return this
        }

        fun withShop(shop: Shop): ReservationBuilder {
            this.shop = shop
            return this
        }

        fun withReserveAt(reserveAt: LocalDateTime): ReservationBuilder {
            this.reserveAt = reserveAt
            return this
        }

        fun build(): Reservation {
            return Reservation.of(
                this.lineItems,
                this.shop,
                this.reserveAt,
                this.reservationStatus
            )
        }
    }

    companion object {
        fun defaultReservation(): ReservationBuilder {
            return ReservationBuilder()
        }
    }
}
