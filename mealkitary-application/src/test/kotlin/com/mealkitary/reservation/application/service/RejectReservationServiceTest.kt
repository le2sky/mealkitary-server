package com.mealkitary.reservation.application.service

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.application.port.output.LoadPaymentPort
import com.mealkitary.reservation.application.port.output.SendRejectedReservationMessagePort
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import com.mealkitary.reservation.domain.payment.PaymentStatus
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class RejectReservationServiceTest : AnnotationSpec() {

    private val loadPaymentPort = mockk<LoadPaymentPort>()
    private val sendRejectedReservationMessagePort = mockk<SendRejectedReservationMessagePort>()
    private val paymentGatewayService = mockk<PaymentGatewayService>()

    private val rejectReservationService =
        RejectReservationService(loadPaymentPort, sendRejectedReservationMessagePort, paymentGatewayService)

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `service unit test - 예약이 정상적으로 거부되면 예약의 상태가 거부됨으로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        payment.approve()
        stubbingPayment(payment)

        rejectReservationService.reject(reservation.id)

        reservation.reservationStatus shouldBe ReservationStatus.REJECTED
    }

    @Test
    fun `service unit test - 예약이 정상적으로 거부되면 결제의 상태가 취소됨으로 변경된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        payment.approve()
        stubbingPayment(payment)

        rejectReservationService.reject(reservation.id)

        payment.paymentStatus shouldBe PaymentStatus.CANCELED
    }

    @Test
    fun `service unit test - 예약이 정상적으로 거부되면 예약 거부 알림이 전송된다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        payment.approve()
        stubbingPayment(payment)

        rejectReservationService.reject(reservation.id)

        verify(exactly = 1) { sendRejectedReservationMessagePort.sendRejectedReservationMessage() }
    }

    @Test
    fun `service unit test - 미결제 상태의 예약을 거부하면 예외를 발생한다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        stubbingPayment(payment)

        shouldThrow<IllegalStateException> {
            rejectReservationService.reject(reservation.id)
        } shouldHaveMessage "미결제 상태인 예약은 거부할 수 없습니다."
    }

    @Test
    fun `service unit test - 이미 승인된 예약은 거부될 수 없다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        payment.approve()
        stubbingPayment(payment)
        reservation.accept()

        shouldThrow<IllegalStateException> {
            rejectReservationService.reject(reservation.id)
        } shouldHaveMessage "이미 예약 확정된 건에 대해서 거부할 수 없습니다."
    }

    @Test
    fun `service unit test - 이미 거부된 예약을 다시 거부할 수 없다`() {
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        payment.approve()
        stubbingPayment(payment)
        rejectReservationService.reject(reservation.id)

        shouldThrow<IllegalStateException> {
            rejectReservationService.reject(reservation.id)
        } shouldHaveMessage "이미 거절된 예약입니다."
    }

    private fun stubbingPayment(payment: Payment) {
        every { loadPaymentPort.loadOnePaymentByReservationId(any()) } answers { payment }
        every { paymentGatewayService.cancel(any(), any()) } answers { }
        every { sendRejectedReservationMessagePort.sendRejectedReservationMessage() } answers {}
    }
}
