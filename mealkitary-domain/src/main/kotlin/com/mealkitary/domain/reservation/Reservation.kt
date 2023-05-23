package com.mealkitary.domain.reservation

import com.mealkitary.domain.shop.Shop
import java.time.LocalDateTime

class Reservation private constructor(
    private val lineItems: List<ReservationLineItem>,
    private val shop: Shop,
    private val reserveAt: LocalDateTime,
    private var reservationStatus: ReservationStatus = ReservationStatus.NOTPAID
) {
    fun reserve() {
        checkValidShop()
        checkReservableTime()
    }

    private fun checkValidShop() {
        if (shop.isInvalid()) {
            throw IllegalArgumentException("유효하지 않은 가게입니다.")
        }
    }

    private fun checkReservableTime() {
        if (!shop.isReservableAt(reserveAt)) {
            throw IllegalArgumentException("예약하시려는 가게는 해당 시간에 예약을 받지 않습니다.")
        }
    }

    fun accept() {
        checkPaidReservation("미결제 상태인 예약은 승인할 수 없습니다.")
        checkAlreadyRejectedForAccept()
        reservationStatus = ReservationStatus.RESERVED
    }

    private fun checkPaidReservation(message: String) {
        if (isNotPaid()) {
            throw IllegalStateException(message)
        }
    }

    private fun checkAlreadyRejectedForAccept() {
        if (isRejected()) {
            throw IllegalStateException("이미 예약 거부된 건에 대해서 승인할 수 없습니다.")
        }
    }

    fun reject() {
        checkPaidReservation("미결제 상태인 예약은 거부할 수 없습니다.")
        checkAlreadyAcceptedForReject()
        reservationStatus = ReservationStatus.REJECTED
    }

    private fun checkAlreadyAcceptedForReject() {
        if (isReserved()) {
            throw IllegalStateException("이미 예약 확정된 건에 대해서 거부할 수 없습니다.")
        }
    }

    fun pay() {
        checkNotPaid()
        reservationStatus = ReservationStatus.PAID
    }

    private fun checkNotPaid() {
        if (!isNotPaid()) {
            throw IllegalStateException("미결제인 상태에서만 결제 상태를 변경할 수 있습니다.")
        }
    }

    fun isReserved() = reservationStatus.isReserved()
    fun isRejected() = reservationStatus.isRejected()
    fun isNotPaid() = reservationStatus.isNotPaid()
    fun isPaid() = reservationStatus.isPaid()

    companion object {
        fun of(
            lineItems: List<ReservationLineItem>,
            shop: Shop,
            reserveAt: LocalDateTime,
            reservationStatus: ReservationStatus
        ): Reservation {
            checkLineItemsAtLeastOne(lineItems)
            checkBeforeTime(reserveAt)
            return Reservation(lineItems, shop, reserveAt, reservationStatus)
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
