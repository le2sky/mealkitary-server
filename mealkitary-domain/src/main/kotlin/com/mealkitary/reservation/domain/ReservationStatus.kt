package com.mealkitary.reservation.domain

enum class ReservationStatus {
    NOTPAID,
    PAID,
    RESERVED,
    REJECTED, ;

    fun isNotPaid(): Boolean {
        return this.equals(NOTPAID)
    }

    fun isReserved(): Boolean {
        return this.equals(RESERVED)
    }

    fun isRejected(): Boolean {
        return this.equals(REJECTED)
    }

    fun isPaid(): Boolean {
        return this.equals(PAID)
    }
}
