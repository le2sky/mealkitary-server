package com.mealkitary.reservation.domain.payment.service

import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService

class CancelPaymentService(
    private val paymentGatewayService: PaymentGatewayService
) {

    fun cancel(payment: Payment) {
        payment.cancel()
        paymentGatewayService.cancel(payment, "점주 측 예약 거부")
    }
}
