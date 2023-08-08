package com.mealkitary.reservation.domain.payment

interface PaymentGatewayService {

    fun confirm(payment: Payment)
}
