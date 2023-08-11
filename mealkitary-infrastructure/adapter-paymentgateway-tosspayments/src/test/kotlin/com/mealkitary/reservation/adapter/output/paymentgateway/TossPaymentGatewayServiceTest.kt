package com.mealkitary.reservation.adapter.output.paymentgateway

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TossPaymentGatewayServiceTest : AnnotationSpec() {

    @Test
    fun `결제 정보를 PG에서 원하는 형식으로 변환한다`() {
        val tossPaymentWebClient = mockk<TossPaymentWebClient>()
        val tossPaymentGatewayService = TossPaymentGatewayService(tossPaymentWebClient)
        val reservation = ReservationTestData.defaultReservation()
            .withReservationStatus(ReservationStatus.NOTPAID)
            .build()
        val payment = Payment.of(
            "paymentKey",
            reservation,
            Money.from(2000)
        )
        val expectedTossPayment = TossPayment.of(
            paymentKey = "paymentKey",
            orderId = reservation.id.toString(),
            2000
        )
        every { tossPaymentWebClient.requestConfirm(any(), any()) } answers {}

        tossPaymentGatewayService.confirm(payment)

        verify { tossPaymentWebClient.requestConfirm(expectedTossPayment, any()) }
    }

    @Test
    fun `HTTP 클라이언트에게 토스 페이먼츠 API 경로를 전달한다`() {
        val tossPaymentWebClient = mockk<TossPaymentWebClient>()
        val tossPaymentGatewayService = TossPaymentGatewayService(tossPaymentWebClient)
        every { tossPaymentWebClient.requestConfirm(any(), any()) } answers {}

        tossPaymentGatewayService.confirm(
            Payment.of(
                "paymentKey",
                ReservationTestData.defaultReservation()
                    .withReservationStatus(ReservationStatus.NOTPAID)
                    .build(),
                Money.from(2000)
            )
        )

        verify { tossPaymentWebClient.requestConfirm(any(), "https://api.tosspayments.com") }
    }
}
