package com.mealkitary.common.data

import com.mealkitary.common.data.ShopTestData.Companion.defaultShop
import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.Reservation
import com.mealkitary.reservation.domain.ReservationLineItem
import com.mealkitary.reservation.domain.ReservationStatus
import com.mealkitary.shop.domain.product.ProductId
import com.mealkitary.shop.domain.shop.Shop
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReservationTestData {

    class ReservationBuilder(
        private var reservationStatus: ReservationStatus = ReservationStatus.NOTPAID,
        private var lineItems: List<ReservationLineItem> = listOf(
            ReservationLineItem(
                ProductId(1L),
                "부대찌개",
                Money.of(1000),
                1
            ),
            ReservationLineItem(
                ProductId(2L),
                "닭볶음탕",
                Money.of(1000),
                1
            )
        ),
        private var shop: Shop = defaultShop().build(),
        private var reserveAt: LocalDateTime = LocalDateTime.of(
            LocalDate.now().plusDays(1),
            LocalTime.of(18, 0)
        )
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
