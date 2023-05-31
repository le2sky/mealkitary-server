package com.mealkitary.reservation.domain

enum class ReservationStatus {

    NONE,
    NOTPAID,
    PAID,
    RESERVED,
    REJECTED;

    fun isNotPaid(): Boolean {
        return this.equals(NOTPAID)
    }

    fun isReserved(): Boolean {
        return this.equals(RESERVED)
    }

    fun isRejected(): Boolean {
        return this.equals(REJECTED)
    }

    fun isNone(): Boolean {
        return this.equals(NONE)
    }
}
