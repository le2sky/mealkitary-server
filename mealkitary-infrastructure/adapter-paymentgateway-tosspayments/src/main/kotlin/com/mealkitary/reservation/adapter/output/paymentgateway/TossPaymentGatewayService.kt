package com.mealkitary.reservation.adapter.output.paymentgateway

import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import org.springframework.stereotype.Component

@Component
class TossPaymentGatewayService(
    private val tossPaymentWebClient: TossPaymentWebClient
) : PaymentGatewayService {

    override fun confirm(payment: Payment) {
        val tossPayment = TossPayment.of(
            paymentKey = payment.paymentKey,
            orderId = payment.reservation.id.toString(),
            amount = payment.amount.value
        )

        tossPaymentWebClient.requestConfirm(tossPayment, "https://api.tosspayments.com")
    }
}
