package com.mealkitary.reservation.application.service

import com.mealkitary.reservation.application.port.input.PayReservationRequest
import com.mealkitary.reservation.application.port.output.LoadReservationPort
import com.mealkitary.reservation.application.port.output.SavePaymentPort
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import com.mealkitary.reservation.domain.reservation.Reservation
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk

class PayReservationServiceTest : AnnotationSpec() {

    private val loadReservationPort = mockk<LoadReservationPort>()
    private val savePaymentPort = mockk<SavePaymentPort>()
    private val paymentGatewayService = mockk<PaymentGatewayService>()

    private val payReservationService =
        PayReservationService(loadReservationPort, savePaymentPort, paymentGatewayService)

    @Test
    fun `service unit test - 신규 결제를 생성한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payReservationRequest = PayReservationRequest(
            "paymentKey",
            reservation.id,
            2000
        )
        stubbingReservation(reservation)

        val result = payReservationService.pay(payReservationRequest)

        result shouldBe reservation.id
    }

    @Test
    fun `service unit test - 결제가 정상적으로 이루어지면 예약의 상태가 결제됨으로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payReservationRequest = PayReservationRequest(
            "paymentKey",
            reservation.id,
            2000
        )
        stubbingReservation(reservation)

        payReservationService.pay(payReservationRequest)

        reservation.reservationStatus shouldBe ReservationStatus.PAID
    }

    @Test
    fun `service unit test - 예약이 미결제 상태가 아니라면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.PAID)
            .build()
        val payReservationRequest = PayReservationRequest(
            "paymentKey",
            reservation.id,
            2000
        )
        stubbingReservation(reservation)

        shouldThrow<IllegalStateException> {
            payReservationService.pay(payReservationRequest)
        } shouldHaveMessage "미결제인 상태에서만 이용 가능한 기능입니다."
    }

    @Test
    fun `service unit test - 결제 금액이 0원 이하라면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payReservationRequest = PayReservationRequest(
            "paymentKey",
            reservation.id,
            0
        )
        stubbingReservation(reservation)

        shouldThrow<IllegalArgumentException> {
            payReservationService.pay(payReservationRequest)
        } shouldHaveMessage "결제 금액은 최소 0원 이상이어야 합니다."
    }

    @Test
    fun `service unit test - 예약 금액과 결제 금액이 일치하지 않으면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payReservationRequest = PayReservationRequest(
            "paymentKey",
            reservation.id,
            20000
        )
        stubbingReservation(reservation)

        shouldThrow<IllegalArgumentException> {
            payReservationService.pay(payReservationRequest)
        } shouldHaveMessage "예약 금액과 결제 금액이 일치하지 않습니다."
    }

    private fun stubbingReservation(reservation: Reservation) {
        every { loadReservationPort.loadOneReservationById(any()) } answers { reservation }
        every { paymentGatewayService.confirm(any()) } answers { }
        every { savePaymentPort.saveOne(any()) } answers { reservation.id }
    }
}
