package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.RejectReservationUseCase
import com.mealkitary.reservation.application.port.output.LoadPaymentPort
import com.mealkitary.reservation.application.port.output.SendRejectedReservationMessagePort
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import com.mealkitary.reservation.domain.payment.service.CancelPaymentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class RejectReservationService(
    private val loadPaymentPort: LoadPaymentPort,
    private val sendRejectedReservationMessagePort: SendRejectedReservationMessagePort,
    paymentGatewayService: PaymentGatewayService
) : RejectReservationUseCase {

    private val cancelPaymentService = CancelPaymentService(paymentGatewayService)

    @Transactional
    override fun reject(reservationId: UUID) {
        val payment = loadPaymentPort.loadOnePaymentByReservationId(reservationId)

        cancelPaymentService.cancel(payment)

        sendRejectedReservationMessagePort.sendRejectedReservationMessage()
    }
}
