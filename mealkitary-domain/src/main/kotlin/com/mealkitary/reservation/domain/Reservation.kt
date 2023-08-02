package com.mealkitary.reservation.domain

import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.ALREADY_PROCESSED_RESERVATION
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.ALREADY_REJECTED_RESERVATION_CANNOT_ACCEPT
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.ALREADY_RESERVED_RESERVATION_CANNOT_REJECT
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.AT_LEAST_ONE_ITEM_REQUIRED
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.FUTURE_TIME_REQUIRED
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.INVALID_RESERVATION_STATUS_FOR_OPERATION
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.INVALID_RESERVATION_STATUS_FOR_PAYMENT
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.INVALID_RESERVE_TIME
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.NOTPAID_RESERVATION_CANNOT_ACCEPT
import com.mealkitary.common.constants.ReservationConstants.Validation.ErrorMessage.NOTPAID_RESERVATION_CANNOT_REJECT
import com.mealkitary.common.model.Money
import com.mealkitary.shop.domain.shop.Shop
import java.time.LocalDateTime
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "reservation")
class Reservation private constructor(
    lineItems: MutableList<ReservationLineItem>,
    shop: Shop,
    reserveAt: LocalDateTime,
    reservationStatus: ReservationStatus
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    var id: Long? = null
        protected set

    @ElementCollection
    @CollectionTable(
        name = "reservation_line_item",
        joinColumns = [JoinColumn(name = "reservation_id")]
    )
    private val lineItems: MutableList<ReservationLineItem> = lineItems

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private val shop: Shop = shop

    @Column(nullable = false)
    private val reserveAt: LocalDateTime = reserveAt

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var reservationStatus: ReservationStatus = reservationStatus
        protected set

    fun calculateTotalPrice(): Money {
        checkNotPaid()

        return lineItems.map { it.calculateEachItemTotalPrice() }
            .reduce { acc, v -> acc + v }
    }

    fun reserve() {
        checkNotNone()
        shop.checkReservableShop()
        checkReservableTime()
        checkEachItem()

        changeReservationStatus(ReservationStatus.NOTPAID)
    }

    private fun checkNotNone() {
        if (!reservationStatus.isNone()) {
            throw IllegalStateException(ALREADY_PROCESSED_RESERVATION.message)
        }
    }

    private fun checkReservableTime() {
        if (!shop.isReservableAt(reserveAt)) {
            throw IllegalArgumentException(INVALID_RESERVE_TIME.message)
        }
    }

    private fun checkEachItem() {
        lineItems.map { it.mapToProduct() }
            .forEach(shop::checkItem)
    }

    fun accept() {
        checkNone()
        checkPaidReservation(NOTPAID_RESERVATION_CANNOT_ACCEPT.message)
        checkAlreadyRejectedForAccept()

        changeReservationStatus(ReservationStatus.RESERVED)
    }

    private fun changeReservationStatus(status: ReservationStatus) {
        reservationStatus = status
    }

    private fun checkPaidReservation(message: String) {
        if (reservationStatus.isNotPaid()) {
            throw IllegalStateException(message)
        }
    }

    private fun checkAlreadyRejectedForAccept() {
        if (reservationStatus.isRejected()) {
            throw IllegalStateException(ALREADY_REJECTED_RESERVATION_CANNOT_ACCEPT.message)
        }
    }

    fun reject() {
        checkNone()
        checkPaidReservation(NOTPAID_RESERVATION_CANNOT_REJECT.message)
        checkAlreadyAcceptedForReject()

        changeReservationStatus(ReservationStatus.REJECTED)
    }

    private fun checkNone() {
        if (reservationStatus.isNone()) {
            throw IllegalStateException(INVALID_RESERVATION_STATUS_FOR_OPERATION.message)
        }
    }

    private fun checkAlreadyAcceptedForReject() {
        if (reservationStatus.isReserved()) {
            throw IllegalStateException(ALREADY_RESERVED_RESERVATION_CANNOT_REJECT.message)
        }
    }

    fun pay() {
        checkNotPaid()

        changeReservationStatus(ReservationStatus.PAID)
    }

    private fun checkNotPaid() {
        if (!reservationStatus.isNotPaid()) {
            throw IllegalStateException(INVALID_RESERVATION_STATUS_FOR_PAYMENT.message)
        }
    }

    companion object {
        fun of(
            lineItems: List<ReservationLineItem>,
            shop: Shop,
            reserveAt: LocalDateTime,
            reservationStatus: ReservationStatus = ReservationStatus.NONE
        ): Reservation {
            checkLineItemsAtLeastOne(lineItems)
            checkBeforeTime(reserveAt)

            return Reservation(lineItems.toMutableList(), shop, reserveAt, reservationStatus)
        }

        private fun checkLineItemsAtLeastOne(lineItems: List<ReservationLineItem>) {
            if (lineItems.isEmpty()) {
                throw IllegalArgumentException(AT_LEAST_ONE_ITEM_REQUIRED.message)
            }
        }

        private fun checkBeforeTime(reserveAt: LocalDateTime) {
            if (reserveAt.isBefore(LocalDateTime.now())) {
                throw IllegalStateException(FUTURE_TIME_REQUIRED.message)
            }
        }
    }
}
