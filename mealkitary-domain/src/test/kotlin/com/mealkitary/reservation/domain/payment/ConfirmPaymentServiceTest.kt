package com.mealkitary.reservation.domain.payment

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ConfirmPaymentServiceTest : AnnotationSpec() {

    private lateinit var paymentGatewayService: PaymentGatewayService

    @BeforeEach
    fun setUp() {
        paymentGatewayService = mockk<PaymentGatewayService>()
    }

    @Test
    fun `유효한 결제라면 결제가 승인된다`() {
        every { paymentGatewayService.confirm(any()) } answers { }
        val confirmPaymentService = ConfirmPaymentService(paymentGatewayService)
        val payment = Payment.of(
            "abc",
            ReservationTestData.defaultReservation().withReservationStatus(ReservationStatus.NOTPAID).build(),
            Money.from(2000)
        )

        confirmPaymentService.confirm(payment)

        payment.paymentStatus shouldBe PaymentStatus.APPROVED
    }

    @Test
    fun `이미 승인된 결제라면 승인 요청을 시도하지 않는다`() {
        every { paymentGatewayService.confirm(any()) } answers { }
        val confirmPaymentService = ConfirmPaymentService(paymentGatewayService)
        val payment = Payment.of(
            "abc",
            ReservationTestData.defaultReservation().withReservationStatus(ReservationStatus.NOTPAID).build(),
            Money.from(2000)
        )
        payment.approve()

        shouldThrow<IllegalStateException> {
            confirmPaymentService.confirm(payment)
        } shouldHaveMessage "이미 승인된 결제는 다시 승인될 수 없습니다."

        verify { paymentGatewayService wasNot Called }
    }

    @Test
    fun `pg 서비스에서 오류가 발생하면 결제를 승인할 수 없다`() {
        every { paymentGatewayService.confirm(any()) } throws (Exception())
        val confirmPaymentService = ConfirmPaymentService(paymentGatewayService)
        val payment = Payment.of(
            "abc",
            ReservationTestData.defaultReservation().withReservationStatus(ReservationStatus.NOTPAID).build(),
            Money.from(2000)
        )

        try {
            confirmPaymentService.confirm(payment)
        } catch (e: Exception) {
            payment.isApproved().shouldBeFalse()
        }
    }
}
