package com.mealkitary.reservation.domain.payment

enum class PaymentStatus {

    READY,
    APPROVED,
    CANCELED;

    fun isApproved(): Boolean {
        return this == APPROVED
    }
}
