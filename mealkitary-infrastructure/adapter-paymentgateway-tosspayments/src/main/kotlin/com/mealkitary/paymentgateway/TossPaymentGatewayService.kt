package com.mealkitary.paymentgateway

import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import org.springframework.stereotype.Component

private const val TOSSPAYMENTS_API_BASE_URL = "https://api.tosspayments.com"

@Component
class TossPaymentGatewayService(
    private val tossPaymentWebClient: TossPaymentWebClient
) : PaymentGatewayService {

    override fun confirm(payment: Payment) {
        val tossPayment = mapToTossPayment(payment)

        tossPaymentWebClient.requestConfirm(tossPayment, TOSSPAYMENTS_API_BASE_URL)
    }

    override fun cancel(payment: Payment, cancelReason: String) {
        val tossPayment = mapToTossPayment(payment)

        tossPaymentWebClient.requestCancel(
            tossPayment,
            TossPaymentCancelPayload(cancelReason),
            TOSSPAYMENTS_API_BASE_URL
        )
    }

    private fun mapToTossPayment(payment: Payment) = TossPayment.of(
        paymentKey = payment.paymentKey,
        orderId = payment.reservation.id.toString(),
        amount = payment.amount.value
    )
}
