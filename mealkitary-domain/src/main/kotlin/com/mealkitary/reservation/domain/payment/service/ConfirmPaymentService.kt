package com.mealkitary.reservation.domain.payment.service

import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService

class ConfirmPaymentService(
    private val paymentGatewayService: PaymentGatewayService
) {

    fun confirm(payment: Payment) {
        payment.approve()
        paymentGatewayService.confirm(payment)
    }
}
