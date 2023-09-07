package com.mealkitary.reservation.domain.payment.service

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.payment.PaymentGatewayService
import com.mealkitary.reservation.domain.payment.PaymentStatus
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CancelPaymentServiceTest : AnnotationSpec() {

    private lateinit var paymentGatewayService: PaymentGatewayService

    @BeforeEach
    fun setUp() {
        paymentGatewayService = mockk<PaymentGatewayService>()
    }

    @Test
    fun `승인된 결제라면 결제를 취소할 수 있다`() {
        every { paymentGatewayService.cancel(any(), any()) } answers { }
        val cancelPaymentService = CancelPaymentService(paymentGatewayService)
        val payment = Payment.of(
            "abc",
            ReservationTestData.defaultReservation().withReservationStatus(ReservationStatus.NOTPAID).build(),
            Money.from(2000)
        )
        payment.approve()

        cancelPaymentService.cancel(payment)

        payment.paymentStatus shouldBe PaymentStatus.CANCELED

        verify(exactly = 1) { paymentGatewayService.cancel(any(), "점주 측 예약 거부") }
    }

    @Test
    fun `이미 취소된 결제라면, 취소 요청을 시도하지 않는다`() {
        every { paymentGatewayService.cancel(any(), any()) } answers { }
        val cancelPaymentService = CancelPaymentService(paymentGatewayService)
        val payment = Payment.of(
            "abc",
            ReservationTestData.defaultReservation().withReservationStatus(ReservationStatus.NOTPAID).build(),
            Money.from(2000)
        )
        payment.approve()
        payment.cancel()

        shouldThrow<IllegalStateException> {
            cancelPaymentService.cancel(payment)
        } shouldHaveMessage "이미 거절된 예약입니다."

        verify { paymentGatewayService wasNot Called }
    }
}
