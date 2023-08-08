package com.mealkitary.reservation.domain.payment

class ConfirmPaymentService(
    private val paymentGatewayService: PaymentGatewayService
) {

    fun confirm(payment: Payment) {
        checkAlreadyApproved(payment)

        paymentGatewayService.confirm(payment)
        payment.approve()
    }

    private fun checkAlreadyApproved(payment: Payment) {
        if (payment.isApproved()) {
            throw IllegalStateException("이미 승인된 결제는 다시 승인될 수 없습니다.")
        }
    }
}
