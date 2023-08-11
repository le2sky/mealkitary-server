package com.mealkitary.reservation.domain.payment

enum class PaymentStatus {

    READY,
    APPROVED;

    fun isApproved(): Boolean {
        return this == APPROVED
    }
}
