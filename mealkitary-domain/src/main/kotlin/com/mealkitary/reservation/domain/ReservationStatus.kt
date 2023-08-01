package com.mealkitary.reservation.domain

enum class ReservationStatus {

    NONE,
    NOTPAID,
    PAID,
    RESERVED,
    REJECTED;

    fun isNotPaid(): Boolean {
        return this == NOTPAID
    }

    fun isReserved(): Boolean {
        return this == RESERVED
    }

    fun isRejected(): Boolean {
        return this == REJECTED
    }

    fun isNone(): Boolean {
        return this == NONE
    }
}
