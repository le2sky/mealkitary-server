package com.mealkitary.reservation.adapter.output.paymentgateway

import com.mealkitary.common.model.Money
import com.mealkitary.reservation.adapter.output.paymentgateway.codec.UrlSafeBase64Codec
import com.mealkitary.reservation.domain.payment.Payment
import com.mealkitary.reservation.domain.reservation.ReservationStatus
import data.ReservationTestData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class TossPaymentMapperTest : AnnotationSpec() {

    @Test
    fun `Payment를 TossPayment로 변환하는 작업을 수행한다`() {
        val codec = UrlSafeBase64Codec()
        val tossPaymentMapper = TossPaymentMapper(codec)

        val result = tossPaymentMapper.mapToTossPayment(
            Payment.of(
                "paymentKey",
                ReservationTestData.defaultReservation()
                    .withReservationStatus(ReservationStatus.NOTPAID)
                    .withId(1234)
                    .build(),
                Money.from(2000)
            )
        )

        result shouldBe TossPayment.of(
            "paymentKey",
            codec.encode("mealkitary:toss:reservation:1234"),
            2000
        )
    }

    @Test
    fun `예약 번호(reservationId)는 널값이 될 수 없다`() {
        val codec = UrlSafeBase64Codec()
        val tossPaymentMapper = TossPaymentMapper(codec)

        shouldThrow<IllegalArgumentException> {
            tossPaymentMapper.mapToTossPayment(
                Payment.of(
                    "paymentKey",
                    ReservationTestData.defaultReservation()
                        .withReservationStatus(ReservationStatus.NOTPAID)
                        .build(),
                    Money.from(2000)
                )
            )
        } shouldHaveMessage "예약 번호(reservationId)는 반드시 존재해야 합니다."
    }

    @Test
    fun `결제의 주문 번호(orderId)는 prefix + reservationId로 인코딩 되어있다`() {
        val codec = UrlSafeBase64Codec()
        val tossPaymentMapper = TossPaymentMapper(codec)

        val result = tossPaymentMapper.mapToTossPayment(
            Payment.of(
                "paymentKey",
                ReservationTestData.defaultReservation()
                    .withReservationStatus(ReservationStatus.NOTPAID)
                    .withId(1234)
                    .build(),
                Money.from(2000)
            )
        ).orderId

        codec.decode(result) shouldBe "mealkitary:toss:reservation:1234"
    }
}
