package com.mealkitary.reservation.application.service

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.application.port.input.PayReservationRequest
import com.mealkitary.reservation.application.port.input.PayReservationUseCase
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SavePaymentPort
import com.mealkitary.reservation.application.port.output.SendNewReservationMessagePort
import com.mealkitary.reservation.domain.payment.ConfirmPaymentService
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import com.mealkitary.reservation.domain.reservation.Reservation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class PayReservationService(
    private val loadReservationPort: LoadReservationPort,
    private val savePaymentPort: SavePaymentPort,
    private val sendNewReservationMessagePort: SendNewReservationMessagePort,
    paymentGatewayService: PaymentGatewayService

) : PayReservationUseCase {

    private val confirmPaymentService = ConfirmPaymentService(paymentGatewayService)

    @Transactional
    override fun pay(payReservationRequest: PayReservationRequest): UUID {
        val reservation = loadReservationPort.loadOneReservationById(payReservationRequest.reservationId)
        val payment = createPayment(payReservationRequest, reservation)

        confirmPaymentService.confirm(payment)

        sendNewReservationMessage(reservation)

        return savePaymentPort.saveOne(payment)
    }

    private fun createPayment(payReservationRequest: PayReservationRequest, reservation: Reservation) =
        Payment.of(payReservationRequest.paymentKey, reservation, Money.from(payReservationRequest.amount))

    private fun sendNewReservationMessage(reservation: Reservation) {
        sendNewReservationMessagePort.sendNewReservationMessage(reservation.id, reservation.buildDescription())
    }
}
